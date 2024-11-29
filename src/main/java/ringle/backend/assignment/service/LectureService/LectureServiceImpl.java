package ringle.backend.assignment.service.LectureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ringle.backend.assignment.aspect.annotation.ValidateLecture;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.converter.LectureConverter;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.repository.LectureRepository;
import ringle.backend.assignment.repository.TutorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final TutorRepository tutorRepository;
    private final LectureConverter lectureConverter;

    @ValidateLecture
    @Override
    @Transactional
    public List<LectureResponseDto.LectureCreateResponse> activateLecture(
            Long tutorId,
            LocalDate startDate, LocalDate endDate,
            TimeSlot startTimeSlot, TimeSlot endTimeSlot) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TUTOR_NOT_FOUND));

        List<Lecture> lectures = new ArrayList<>();

        // 시작 날짜부터 종료 날짜까지 반복하며 수업 등록
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (TimeSlot timeSlot : TimeSlot.values()) {
                if (timeSlot.compareTo(startTimeSlot) >= 0 && timeSlot.compareTo(endTimeSlot) <= 0) {
                    boolean exists = lectureRepository.existsByTutorAndDateAndStartTimeSlot(tutor, date, timeSlot);
                    if (!exists) {
                        Lecture lecture = new Lecture();
                        lecture.setTutor(tutor);
                        lecture.setDate(date);
                        lecture.setStartTimeSlot(timeSlot);
                        lecture.setLectureType(LectureType._30_MIN);
                        lecture.setAvailable(true);

                        lectureRepository.save(lecture);
                        lectures.add(lecture);
                    } else
                        throw new TempHandler(ErrorStatus.LECTURE_BAD_REQUEST);
                }
            }
        }
        return lectureConverter.toLectureResponseDtos(lectures);
    }

    @Override
    public LectureResponseDto.LectureDeleteResponse deleteLecture(Long tutorId, Long lectureId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TUTOR_NOT_FOUND));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.LECTURE_NOT_FOUND));

        if (!Objects.equals(lecture.getTutor().getId(), lectureId))
            throw new TempHandler(ErrorStatus.TUTOR_FORBIDDEN);

        lectureRepository.delete(lecture);
        return LectureConverter.lectureDeleteResponseDto(lectureId, "수업 가능 시간대가 성공적으로 비활성화되었습니다.");
    }

    @Override
    public List<LectureResponseDto.LectureGetResponse> getAvailableTutors(LocalDate date, TimeSlot timeSlot, LectureType lectureType) {
        List<Tutor> tutors = tutorRepository.findAllWithAvailableLectures();
        // FetchJoin으로 N+1 문제 해결
        // 기존 코드 : tutorRepository.findAll() 로 모든 튜터의 데이터 로드
        List<LectureResponseDto.LectureGetResponse> responseDtos = new ArrayList<>();

        for (Tutor tutor : tutors) {
            // 튜터와 연관된 강의 데이터를 필터링
            List<Lecture> availableLectures = tutor.getLectures().stream()
                    .filter(lecture -> lecture.getDate().equals(date) // 날짜 필터
                            && lecture.getStartTimeSlot().equals(timeSlot) // 타임슬롯 필터
                            && lecture.isAvailable()) // 예약 가능 여부 필터
                    .collect(Collectors.toList());

            if (availableLectures.isEmpty()) continue; // 조건에 맞는 강의가 없으면 건너뛰기

            // If LectureType is _60_MIN, check the next slot as well
            if (lectureType == LectureType._60_MIN) {
                TimeSlot nextSlot = TimeSlot.getNextSlot(timeSlot);
                if (nextSlot == null) continue; // Skip if there's no next slot

                // Check if the next slot is available
                boolean isNextSlotAvailable = lectureRepository.existsByTutorAndDateAndStartTimeSlotAndIsAvailableTrue(tutor, date, nextSlot);

                // Console logs
//                System.out.println("튜터: " + tutor.getName());
//                System.out.println("현재 슬롯: " + timeSlot + ", 다음 슬롯: " + nextSlot);
//                System.out.println("다음 슬롯 가능 여부: " + isNextSlotAvailable);

                if (!isNextSlotAvailable) continue; // Skip if the next slot is not available
            }

            TimeSlot previousSlot = TimeSlot.getPreviousSlot(timeSlot);
            TimeSlot nextSlot = TimeSlot.getNextSlot(timeSlot);

            Optional<Lecture> previousLecture = previousSlot != null
                    ? lectureRepository.findByTutorAndDateAndStartTimeSlot(tutor, date, previousSlot)
                    : Optional.empty();

            Optional<Lecture> nextLecture = nextSlot != null
                    ? lectureRepository.findByTutorAndDateAndStartTimeSlot(tutor, date, nextSlot)
                    : Optional.empty();

            // Updated TimeSlotInfo to include lectureId
            LectureResponseDto.LectureGetResponse.TimeSlotInfo previousSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    previousSlot != null ? previousSlot.name() : "N/A",
                    previousLecture.map(Lecture::isAvailable).orElse(false),
                    previousLecture.map(Lecture::getId).orElse(null)
            );

            LectureResponseDto.LectureGetResponse.TimeSlotInfo currentSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    timeSlot.name(),
                    availableLectures.get(0).isAvailable(),
                    availableLectures.get(0).getId()
            );

            LectureResponseDto.LectureGetResponse.TimeSlotInfo nextSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    nextSlot != null ? nextSlot.name() : "N/A",
                    nextLecture.map(Lecture::isAvailable).orElse(false),
                    nextLecture.map(Lecture::getId).orElse(null)
            );

            LectureResponseDto.LectureGetResponse dto = LectureResponseDto.LectureGetResponse.builder()
                    .lectureId(availableLectures.get(0).getId())
                    .tutorId(tutor.getId())
                    .tutorName(tutor.getName())
                    .tutorMajor(tutor.getMajor())
                    .tutorUniv(tutor.getUniversity())
                    .date(String.valueOf(date))
                    .timeSlots(new LectureResponseDto.LectureGetResponse.TimeSlots(previousSlotInfo, currentSlotInfo, nextSlotInfo))
                    .isAvailable(availableLectures.get(0).isAvailable())
                    .build();

            responseDtos.add(dto);
        }

        return responseDtos;
    }




    @Override
    public List<LectureResponseDto.LecturesGetResponseForTutor> getAvailableLectures(LocalDate startDate, LocalDate endDate, LectureType lectureType) {
        List<Tutor> tutors = tutorRepository.findAllWithAvailableLectures();
        // FetchJoin으로 N+1 문제 해결
        // 기존 코드 : tutorRepository.findAll() 로 모든 데이터 로드

        List<LectureResponseDto.LecturesGetResponseForTutor> responseDtos = new ArrayList<>();

        for (Tutor tutor : tutors) {
            // 필터링: startDate와 endDate 사이의 예약 가능한 강의만 추출
            List<Lecture> filteredLectures = tutor.getLectures().stream()
                    .filter(lecture -> lecture.getDate().isAfter(startDate.minusDays(1)) // startDate 포함
                            && lecture.getDate().isBefore(endDate.plusDays(1)) // endDate 포함
                            && lecture.isAvailable()) // 예약 가능 여부 확인
                    .collect(Collectors.toList());

            if (!filteredLectures.isEmpty()) {
                // Lecture 데이터를 DTO로 변환
                LectureResponseDto.LecturesGetResponseForTutor dto = lectureConverter.toLectureGetResponseForTutor(tutor, filteredLectures);
                responseDtos.add(dto);
            }
        }
        return responseDtos;
    }
}
