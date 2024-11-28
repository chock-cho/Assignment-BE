package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureDeleteResponse{
        private Long lectureId; // 삭제한 lectureId
        private String message;
    }
}
