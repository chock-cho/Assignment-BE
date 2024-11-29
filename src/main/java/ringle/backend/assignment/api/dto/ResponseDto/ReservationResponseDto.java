package ringle.backend.assignment.api.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ringle.backend.assignment.domain.enums.LectureType;

public class ReservationResponseDto {
    @Builder
    @Getter
    public static class ReservationCreateResponse{
        private Long reservationId; // 수업의 예약 id
        private String reservedLectureType; // 수업의 타입(30분, 60분)
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationInfoResponse {

        private Long reservationId;
        private String reservedLectureType; // 예약된 수업 타입 (30분, 60분)
        private TutorInfo tutorInfo;
        private LectureInfo lectureInfo;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TutorInfo {
            private Long tutorId;
            private String tutorName;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LectureInfo {
            private LectureDetail startLecture;
            private LectureDetail endLecture;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LectureDetail {
            private Long lectureId;
            private String lectureTimeSlot;
        }
    }
}
