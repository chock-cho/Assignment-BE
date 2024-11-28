package ringle.backend.assignment.service.LectureService;

import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;

import java.util.List;

public interface LectureService {

    // 날짜+시간대+duration 지정하여 수업 등록 메서드
    List<LectureResponseDto.LectureCreateResponse> activateLecture(LectureRequestDto.LectureCreateRequest req);

    // 삭제 요청한 튜터의 id + 삭제할 수업 삭제 메서드
    void deleteLecture(LectureRequestDto.LectureDeleteRequest req);
}
