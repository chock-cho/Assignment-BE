package ringle.backend.assignment.service.LectureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService{

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
                    }
                    else
                        throw new TempHandler(ErrorStatus.LECTURE_BAD_REQUEST);
                }
            }
        }
        return lectureConverter.toLectureResponseDtos(lectures);
        }

    @Override
    public LectureResponseDto.LectureDeleteResponse deleteLecture(LectureRequestDto.LectureDeleteRequest req){
        Tutor tutor = tutorRepository.findById(req.getTutorId())
                .orElseThrow(()-> new TempHandler(ErrorStatus.TUTOR_NOT_FOUND));

        Lecture lecture = lectureRepository.findById(req.getLectureId())
                .orElseThrow(()-> new TempHandler(ErrorStatus.LECTURE_NOT_FOUND));

        if(!Objects.equals(lecture.getTutor().getId(), req.getTutorId()))
            throw new TempHandler(ErrorStatus.TUTOR_FORBIDDEN);

        lectureRepository.delete(lecture);
        return LectureConverter.lectureDeleteResponseDto(req.getLectureId(), "수업 가능 시간대가 성공적으로 비활성화되었습니다.");
    }
}
