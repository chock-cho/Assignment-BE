package ringle.backend.assignment.converter;

import org.springframework.stereotype.Component;
import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Student;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.mapping.Reservation;
import ringle.backend.assignment.service.ReservationService.ReservationService;

@Component
public class ReservationConverter {

    public Reservation toEntity(Lecture startLecture, Lecture endLecture, Student student, Tutor tutor, LectureType lectureType) {
        return Reservation.builder()
                .lectureId1(startLecture)
                .lectureId2(endLecture)
                .student(student)
                .tutor(tutor)
                .lectureType(lectureType)
                .build();
    }

    public static ReservationResponseDto.ReservationCreateResponse toCreateResponseDto(Reservation reservation){
        return ReservationResponseDto.ReservationCreateResponse.builder()
                .reservationId(reservation.getId())
                .reservedLectureType(String.valueOf(reservation.getLectureType()))
                .message("예약이 성공적으로 완료되었습니다.")
                .build();
    }
}

