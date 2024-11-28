package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;

public class LectureResponseDto {
    @Builder
    @Getter
    public static class LectureCreateResponse{
        private Long id;
        private String tutorName;
        private String tutorMajor;
        private String tutorUniv;
        private String date;
        private String timeSlots;
        private boolean isAvailable;
    }
}
