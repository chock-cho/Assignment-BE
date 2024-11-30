package ringle.backend.assignment.service.ReservationService;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.aspect.annotation.ValidateReservation;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;
import ringle.backend.assignment.converter.ReservationConverter;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Student;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.mapping.Reservation;
import ringle.backend.assignment.repository.LectureRepository;
import ringle.backend.assignment.repository.ReservationRepository;
import ringle.backend.assignment.repository.StudentRepository;

import java.util.List;

import static ringle.backend.assignment.domain.enums.LectureType._30_MIN;
import static ringle.backend.assignment.domain.enums.LectureType._60_MIN;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    private final LectureRepository lectureRepository;
    private final ReservationConverter reservationConverter;
    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    @Override
    @Transactional
    @ValidateReservation
    public ReservationResponseDto.ReservationCreateResponse makeReservation(
            Long studentId,
            ReservationRequestDto.ReservationCreateRequest req){
        try {
            Lecture startLecture = lectureRepository.findById(req.getStartLectureId())
                    .orElseThrow(() -> new TempHandler(ErrorStatus.RESERVATION_NOT_FOUND));
            Lecture endLecture = lectureRepository.findById(req.getEndLectureId())
                    .orElseThrow(() -> new TempHandler(ErrorStatus.RESERVATION_NOT_FOUND));

            LectureType reqlectureType = null;
            if (!startLecture.getTutor().getId().equals(req.getTutorId())
                    || !endLecture.getTutor().getId().equals(req.getTutorId()))
                throw new TempHandler(ErrorStatus.RESERVATION_NOT_FOUND);

            if (areLecturesConsecutive(startLecture, endLecture) == -1)
                // 수업 신청 시 30분, 60분 단위로 수업 신청 가능, 연속된 타임슬롯만 신청 가능
                throw new TempHandler(ErrorStatus.RESERVATION_INVALID_REQEUST);
            else if (areLecturesConsecutive(startLecture, endLecture) == 1)
                reqlectureType = _30_MIN;
            else if (areLecturesConsecutive(startLecture, endLecture) == 2)
                reqlectureType = _60_MIN;

            Student reqStudent = studentRepository.findById(studentId).orElseThrow(
                    () -> new TempHandler(ErrorStatus.STUDENT_NOT_FOUND));

            Tutor reqTutor = startLecture.getTutor(); // 해당 수업에 해당하는 튜터 정보

            Reservation newReservation = reservationConverter.toEntity(startLecture, endLecture, reqStudent, reqTutor, reqlectureType);
            reservationRepository.save(newReservation);

            startLecture.setAvailable(false);
            endLecture.setAvailable(false);
            lectureRepository.save(startLecture);
            lectureRepository.save(endLecture);

            return ReservationConverter.toCreateResponseDto(newReservation);
        } catch (OptimisticLockException e){
            throw new TempHandler(ErrorStatus.RESERVATION_CONFLICT);
        }
    }

    private int areLecturesConsecutive(Lecture startLecture, Lecture endLecture) {
        // 시작 TimeSlot과 끝 TimeSlot이 연속적인지 확인
        int startTimeSlot = startLecture.getStartTimeSlot().getOrder();
        int endTimeSlot = endLecture.getStartTimeSlot().getOrder();
        if(startTimeSlot == endTimeSlot) return 1; // 30분 수업
        else if(startTimeSlot + 1 == endTimeSlot) return 2; // 60분 수업
        else return -1; // 오류 처리
    }

}
