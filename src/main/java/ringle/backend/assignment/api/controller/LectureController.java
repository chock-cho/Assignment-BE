package ringle.backend.assignment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ringle.backend.assignment.api.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.service.LectureService.LectureService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tutor")
@Tag(name = "${swagger.tag.lecture}")

public class LectureController {
    private final LectureService lectureService;

    @PostMapping("/lecture")
    @Operation(summary = "수업 가능한 시간대 생성 API",
            description = """
                     튜터가 **수업 가능한 시간대**를 생성합니다. \s
                     
                     ⚠️ TimeSlot: 한 슬롯 당 30분 단위임에 유의합니다. \s
                     ⚠️ TimeSlot을 입력 시에, `00_00`에 시작할 시각의 HH:MM를 입력합니다.\s
                        (ex. SLOT_14_30=14시 30분에 시작하는 수업 시각)\s
                    \s
                     [body] \s
                     * startDate: 수업 가능한 시간대 시작 날짜(String) - "YYYY-MM-DD", \s
                     * endDate: 수업 가능한 시간대 종료 날짜(String) - "YYYY-MM-DD" , \s
                     * startTimeSlot: 수업 가능한 시간대 시작 시각(String) - "SLOT_00_00", \s
                     * endTimeSlot: 수업 가능한 시간대 종료 시각(String) - "SLOT_00_00", \s
                     * tutorId: 시간대를 생성하려는 튜터 id(Long)
                    \s""")
    public ApiResponse<List<LectureResponseDto.LectureCreateResponse>> makeAvaliableTimes(LectureRequestDto.LectureCreateRequest req) {
        Long tutorId = req.getTutorId();
        // -- 사용자 validate 로직 추가 되어야 함 --//
        return ApiResponse.onSuccess(lectureService.activateLecture(req));
    }
}
