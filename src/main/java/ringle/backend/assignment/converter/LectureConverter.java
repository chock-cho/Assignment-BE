package ringle.backend.assignment.converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.domain.Lecture;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LectureConverter {


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
                .timeSlots(lecture.getTimeSlot().toString())
                .isAvailable(lecture.isAvailable())
                .build();
    }
}
