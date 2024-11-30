package ringle.backend.assignment.service.LectureService;

import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface LectureQueryService {
    // 학생: 특정 날짜 선택 후, 시간대 & 수업 길이로 수업 가능한 튜터 조회
    public List<LectureResponseDto.LectureGetResponse> getAvailableTutors(LocalDate date, TimeSlot timeSlot, LectureType lectureType);

    // 학생: 기간(시작 날짜, 종료 날짜) & 수업 길이로 가능한 수업 조회
    public List<LectureResponseDto.LecturesGetResponseForTutor> getAvailableLectures(LocalDate startDate, LocalDate endDate, LectureType lectureType);
}
