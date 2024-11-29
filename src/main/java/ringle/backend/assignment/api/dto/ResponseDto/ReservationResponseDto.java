package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import ringle.backend.assignment.domain.enums.LectureType;

public class ReservationResponseDto {
    @Builder
    @Getter
    public static class ReservationCreateResponse{
        private Long reservationId; // 수업의 예약 id
        private String reservedLectureType; // 수업의 타입(30분, 60분)
        private String message;
    }
}
