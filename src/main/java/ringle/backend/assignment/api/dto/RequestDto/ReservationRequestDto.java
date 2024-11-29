package ringle.backend.assignment.api.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

public class ReservationRequestDto {

    @Builder
    @Getter
    public static class ReservationCreateRequest {

        @NotNull(message = "튜터 ID를 지정해주세요.")
        @JsonProperty("tutorId")
        private Long tutorId;

        @NotNull(message = "예약할 수업의 날짜를 지정해주세요.")
        @JsonProperty("lectureDate") // 날짜(YYYY-MM-DD) 형식
        private String lectureDate;

        @NotNull(message = "예약할 수업의 시작 시간을 지정해주세요.")
        @JsonProperty("timeSlot")
        private TimeSlot timeSlot; // 수업 시작 시각에 해당하는 타임 슬롯

        @NotNull(message = "예약할 수업의 타입(총 수업 시간)을 지정해주세요.")
        @JsonProperty("duration")
        private LectureType lectureType; // 수업 타입 : (30min, 60min 중 하나)
    }

}
