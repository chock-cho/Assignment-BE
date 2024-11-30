package ringle.backend.assignment.service.LectureService;

import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface LectureService {

    // 기간(시작 날짜, 종료 날짜), 시간(startTimeSlot, endTimeSlot) 지정하여 수업 등록 메서드
    //       - 기간과 시간을 한 번에 등록할 수 있어 사용자 경험 개선
    public List<LectureResponseDto.LectureCreateResponse> activateLecture(
            Long tutorId, LocalDate startDate, LocalDate endDate, TimeSlot startTimeSlot, TimeSlot endTimeSlot);
    // 삭제 요청한 튜터의 id + 삭제할 수업 삭제 메서드
    LectureResponseDto.LectureDeleteResponse deleteLecture(Long tutorId, Long lectureId);
}
