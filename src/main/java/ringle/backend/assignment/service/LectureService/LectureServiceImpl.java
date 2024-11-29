package ringle.backend.assignment.service.LectureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;
import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
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

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final TutorRepository tutorRepository;
    private final LectureConverter lectureConverter;

    @Override
    @Transactional
    public List<LectureResponseDto.LectureCreateResponse> activateLecture(LectureRequestDto.LectureCreateRequest req) {
        Tutor tutor = tutorRepository.findById(req.getTutorId())
                .orElseThrow(() -> new TempHandler(ErrorStatus.TUTOR_NOT_FOUND));

        List<Lecture> lectures = new ArrayList<>();

        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();
        TimeSlot startTimeSlot = req.getStartTimeSlot();
        TimeSlot endTimeSlot = req.getEndTimeSlot();
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
    public LectureResponseDto.LectureDeleteResponse deleteLecture(LectureRequestDto.LectureDeleteRequest req) {
        Tutor tutor = tutorRepository.findById(req.getTutorId())
                .orElseThrow(() -> new TempHandler(ErrorStatus.TUTOR_NOT_FOUND));

        Lecture lecture = lectureRepository.findById(req.getLectureId())
                .orElseThrow(() -> new TempHandler(ErrorStatus.LECTURE_NOT_FOUND));

        if (!Objects.equals(lecture.getTutor().getId(), req.getTutorId()))
            throw new TempHandler(ErrorStatus.TUTOR_FORBIDDEN);

        lectureRepository.delete(lecture);
        return LectureConverter.lectureDeleteResponseDto(req.getLectureId(), "수업 가능 시간대가 성공적으로 비활성화되었습니다.");
    }

    @Override
    public List<LectureResponseDto.LectureGetResponse> getAvailableTutors(LocalDate date, TimeSlot timeSlot, LectureType lectureType) {
        List<Tutor> tutors = tutorRepository.findAll();
        List<LectureResponseDto.LectureGetResponse> responseDtos = new ArrayList<>();

        for (Tutor tutor : tutors) {
            List<Lecture> availableLectures = lectureRepository.findByTutorAndDateAndStartTimeSlotAndIsAvailableTrue(tutor, date, timeSlot);
            if (availableLectures.isEmpty()) {
                System.out.println("튜터: " + tutor.getName() + "에 대해 사용 가능한 수업이 없습니다.");
                continue;
            }
            // LectureType이 _60_MIN인 경우, 다음 슬롯도 함께 체크 (슬롯이 30분 단위이기 때문에) //
            if (lectureType == LectureType._60_MIN) {

                TimeSlot nextSlot = TimeSlot.getNextSlot(timeSlot);
                if (nextSlot == null) continue; // 다음 슬롯이 없는 경우 건너뜀

                // 다음 슬롯이 예약 가능한지 확인
                boolean isNextSlotAvailable = lectureRepository.existsByTutorAndDateAndStartTimeSlotAndIsAvailableTrue(tutor, date, nextSlot);

                // 콘솔 log
                System.out.println("튜터: " + tutor.getName());
                System.out.println("현재 슬롯: " + timeSlot + ", 다음 슬롯: " + nextSlot);
                System.out.println("다음 슬롯 가능 여부: " + isNextSlotAvailable);

                if (!isNextSlotAvailable) continue; // 다음 슬롯 예약이 가능하지 않으면 해당 수업 건너뜀
            }

            TimeSlot previousSlot = TimeSlot.getPreviousSlot(timeSlot);
            TimeSlot nextSlot = TimeSlot.getNextSlot(timeSlot);

            Optional<Lecture> previousLecture = previousSlot != null
                    ? lectureRepository.findByTutorAndDateAndStartTimeSlot(tutor, date, previousSlot)
                    : Optional.empty();

            Optional<Lecture> nextLecture = nextSlot != null
                    ? lectureRepository.findByTutorAndDateAndStartTimeSlot(tutor, date, nextSlot)
                    : Optional.empty();

            LectureResponseDto.LectureGetResponse.TimeSlotInfo previousSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    previousSlot != null ? previousSlot.name() : "N/A",
                    previousLecture.map(Lecture::isAvailable).orElse(false)
            );

            LectureResponseDto.LectureGetResponse.TimeSlotInfo currentSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    timeSlot.name(),
                    availableLectures.get(0).isAvailable()
            );

            LectureResponseDto.LectureGetResponse.TimeSlotInfo nextSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                    nextSlot != null ? nextSlot.name() : "N/A",
                    nextLecture.map(Lecture::isAvailable).orElse(false)
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
        List<Tutor> tutors = tutorRepository.findAll();
        List<LectureResponseDto.LecturesGetResponseForTutor> responseDtos = new ArrayList<>();

        for (Tutor tutor : tutors) {
            List<Lecture> lectures = lectureRepository.findByTutorAndDateBetweenAndIsAvailableTrue(tutor, startDate, endDate);
            if (!lectures.isEmpty()) {
                LectureResponseDto.LecturesGetResponseForTutor dto = lectureConverter.toLectureGetResponseForTutor(tutor, lectures);
                responseDtos.add(dto);
            }
        }
        return responseDtos;
    }
}
