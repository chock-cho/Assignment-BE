package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.*;

import java.util.List;

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
        private Long lectureId;
        private Long tutorId;
        private String tutorName;
        private String tutorMajor;
        private String tutorUniv;
        private TimeSlots timeSlots;
        private boolean isAvailable;

        @Data
        @AllArgsConstructor
        public static class TimeSlotInfo {
            private String name;
            private boolean isAvailable;
            private Long lectureId;
        }

        @Data
        @AllArgsConstructor
        public static class TimeSlots {
            private TimeSlotInfo previousSlot;
            private TimeSlotInfo currentSlot;
            private TimeSlotInfo nextSlot;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LecturesGetResponseForTutor{
        private Long tutorId;
        private String tutorName;
        private String tutorMajor;
        private String tutorUniv;
        private String date;
        private List<LectureInfo> lectures;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LectureInfo {
            private Long lectureId;
            private String timeSlot;
            private boolean isAvailable;
        }
    }
}
