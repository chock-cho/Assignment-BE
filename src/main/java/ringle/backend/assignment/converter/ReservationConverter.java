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

import java.util.List;

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

    // 신청한 수업에 대한 예약 내역 조회

    public List<ReservationResponseDto.ReservationInfoResponse> toReservationInfoResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toReservationInfoResponse)
                .toList();
    }

    public ReservationResponseDto.ReservationInfoResponse toReservationInfoResponse(Reservation reservation) {
        String reservedLectureType = reservation.getLectureType().name();
        ReservationResponseDto.ReservationInfoResponse.LectureInfo lectureInfo = ReservationResponseDto.ReservationInfoResponse.LectureInfo.builder()
                .startLecture(toLectureDetail(reservation.getLectureId1()))
                .endLecture("_30_MIN".equals(reservedLectureType) ? null : toLectureDetail(reservation.getLectureId2()))
                .build();

        return ReservationResponseDto.ReservationInfoResponse.builder()
                .reservationId(reservation.getId())
                .reservedLectureType(reservedLectureType)
                .tutorInfo(ReservationResponseDto.ReservationInfoResponse.TutorInfo.builder()
                        .tutorId(reservation.getTutor().getId())
                        .tutorName(reservation.getTutor().getName())
                        .build())
                .lectureInfo(lectureInfo)
                .build();
    }

    private ReservationResponseDto.ReservationInfoResponse.LectureDetail toLectureDetail(Lecture lecture) {
        return ReservationResponseDto.ReservationInfoResponse.LectureDetail.builder()
                .lectureId(lecture.getId())
                .lectureTimeSlot(lecture.getStartTimeSlot().name())
                .build();
    }
}

