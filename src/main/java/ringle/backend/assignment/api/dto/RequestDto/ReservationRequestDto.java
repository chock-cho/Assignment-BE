package ringle.backend.assignment.api.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.util.List;

public class ReservationRequestDto {

    @Builder
    @Getter
    public static class ReservationCreateRequest {

        @NotNull(message = "예약할 수업의 튜터 ID를 지정해주세요.")
        @JsonProperty("tutorId")
        private Long tutorId;

        @NotNull(message = "예약할 수업의 시작하는 수업의 ID를 입력해주세요.")
        @JsonProperty("startLectureId")
        private Long startLectureId; // 시작하는 수업의 ID

        @NotNull(message = "예약할 수업의 끝나는 수업의 ID를 입력해주세요.")
        @JsonProperty("endLectureId")
        private Long endLectureId; // 끝나는 수업의 ID
    }

}
