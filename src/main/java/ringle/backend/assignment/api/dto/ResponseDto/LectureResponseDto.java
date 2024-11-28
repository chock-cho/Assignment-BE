package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.*;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureGetResponse{
        private Long id;
        private String tutorName;
        private String tutorMajor;
        private String tutorUniv;
        private String date;
        private TimeSlots timeSlots;
        private boolean isAvailable;

        @Data
        @AllArgsConstructor
        public static class TimeSlotInfo {
            private String name;
            private boolean isAvailable;
        }

        @Data
        @AllArgsConstructor
        public static class TimeSlots {
            private TimeSlotInfo previousSlot;
            private TimeSlotInfo currentSlot;
            private TimeSlotInfo nextSlot;
        }
    }
}
