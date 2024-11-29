package ringle.backend.assignment.converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.repository.LectureRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LectureConverter {

    private final LectureRepository lectureRepository;

    public List<LectureResponseDto.LectureCreateResponse> toLectureResponseDtos(List<Lecture> lectures) {
        return lectures.stream()
                .map(this::toLectureResponseDto)
                .collect(Collectors.toList());
    }
    @Builder
    public LectureResponseDto.LectureCreateResponse toLectureResponseDto(Lecture lecture) {
        return LectureResponseDto.LectureCreateResponse.builder()
                .id(lecture.getId())
                .tutorName(lecture.getTutor().getName())
                .tutorMajor(lecture.getTutor().getMajor())
                .tutorUniv(lecture.getTutor().getUniversity())
                .date(String.valueOf(lecture.getDate()))
                .timeSlots(lecture.getStartTimeSlot().toString())
                .isAvailable(lecture.isAvailable())
                .build();
    }

    public static LectureResponseDto.LectureDeleteResponse lectureDeleteResponseDto(Long lectureId, String message) {
        return LectureResponseDto.LectureDeleteResponse.builder()
                .lectureId(lectureId)
                .message(message)
                .build();
    }

    // lecture 엔티티 -> LectureGetResponse DTO로 전환
    public LectureResponseDto.LectureGetResponse toLectureGetResponse(Lecture lecture, Lecture previousLecture, Lecture nextLecture) {
        LectureResponseDto.LectureGetResponse.TimeSlotInfo previousSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                previousLecture != null ? previousLecture.getStartTimeSlot().name() : "N/A",
                previousLecture != null && previousLecture.isAvailable(),
                Objects.requireNonNull(previousLecture).getId()
        );

        LectureResponseDto.LectureGetResponse.TimeSlotInfo currentSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                lecture.getStartTimeSlot().name(),
                lecture.isAvailable(),
                Objects.requireNonNull(previousLecture).getId()
        );

        LectureResponseDto.LectureGetResponse.TimeSlotInfo nextSlotInfo = new LectureResponseDto.LectureGetResponse.TimeSlotInfo(
                nextLecture != null ? nextLecture.getStartTimeSlot().name() : "N/A",
                nextLecture != null && nextLecture.isAvailable(),
                Objects.requireNonNull(previousLecture).getId()
        );

        return LectureResponseDto.LectureGetResponse.builder()
                .lectureId(lecture.getId())
                .tutorId(lecture.getTutor().getId())
                .tutorName(lecture.getTutor().getName())
                .date(String.valueOf(lecture.getDate()))
                .timeSlots(new LectureResponseDto.LectureGetResponse.TimeSlots
                        (previousSlotInfo, currentSlotInfo, nextSlotInfo))
                .isAvailable(lecture.isAvailable())
                .build();
    }

    public LectureResponseDto.LecturesGetResponseForTutor toLectureGetResponseForTutor(Tutor tutor, List<Lecture> lectures) {
        List<LectureResponseDto.LecturesGetResponseForTutor.LectureInfo> lectureInfos = new ArrayList<>();
        for (Lecture lecture : lectures) {
            LectureResponseDto.LecturesGetResponseForTutor.LectureInfo lectureInfo = LectureResponseDto.LecturesGetResponseForTutor.LectureInfo.builder()
                    .lectureId(lecture.getId())
                    .timeSlot(lecture.getStartTimeSlot().name())
                    .isAvailable(lecture.isAvailable())
                    .build();
            lectureInfos.add(lectureInfo);
        }

        return LectureResponseDto.LecturesGetResponseForTutor.builder()
                .tutorId(tutor.getId())
                .tutorName(tutor.getName())
                .tutorMajor(tutor.getMajor())
                .tutorUniv(tutor.getUniversity())
                .lectures(lectureInfos)
                .build();
    }
}
