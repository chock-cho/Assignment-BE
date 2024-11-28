package ringle.backend.assignment.api.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;

public class LectureRequestDto {

    @Builder
    @Getter
    public static class LectureCreateRequest {
        @JsonProperty("tutorId")
        @NotNull(message = "튜터 ID를 지정해주세요.")
        private Long tutorId;

        @NotNull(message = "활성화할 수업들의 시작 날짜를 지정해주세요.")
        private LocalDate startDate;

        @NotNull(message = "활성화할 수업들의 종료 날짜를 지정해주세요.")
        private LocalDate endDate;

        @NotNull(message = "활성화할 수업들의 시작 시간을 지정해주세요.")
        private TimeSlot startTimeSlot;

        @NotNull(message = "활성화할 수업들의 종료 시간을 지정해주세요.")
        private TimeSlot endTimeSlot;
    }

    @Getter
    public static class LectureDeleteRequest {
        @JsonProperty("tutorId")
        @NotNull(message = "당신의 ID를 입력해주세요.")
        // 본인이 등록한 lectures에 대해서만 삭제 권한을 가지고 있어야 한다.
        private Long tutorId;

        @JsonProperty("lectureId")
        @NotNull(message = "비활성화(삭제)할 수업의 ID를 입력해주세요.")
        private Long lectureId;
    }
}
