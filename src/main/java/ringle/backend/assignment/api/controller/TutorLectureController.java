package ringle.backend.assignment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ringle.backend.assignment.aspect.annotation.ValidateLecture;
import ringle.backend.assignment.aspect.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.service.LectureService.LectureService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tutor")
@Tag(name = "${swagger.tag.tutor_lecture}")

public class TutorLectureController {
    private final LectureService lectureService;

    @PostMapping("/lecture")
    @ValidateLecture
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
    public ApiResponse<List<LectureResponseDto.LectureCreateResponse>> makeAvailableTimes(
            @RequestBody @Valid LectureRequestDto.LectureCreateRequest req) {
        // -- 사용자 validate 로직 추가 되어야 함 --//
        return ApiResponse.onSuccess(lectureService.activateLecture(req));
    }

    @DeleteMapping("/lecture")
    @ValidateLecture
    @Operation(summary = "수업 가능한 시간대 삭제 API",
            description = """
                     튜터가 **수업 가능한(활성화된) 시간대: Lecture**를 삭제합니다. \s
                     
                     ⚠️ Lecture: 한 슬롯 당 30분 단위임에 유의합니다. \s
                     ⚠️ 기존에 본인이 등록한 lecture 대해서만 삭제할 수 있습니다.\s
                    \s
                     [body] \s
                     * tutorId: 시간대를 비활성화하려는 본인=튜터 id(Long), \s
                     * lectureId: 비활성화하려는 Lecture id(Long)\s
                    \s""")
    public ApiResponse<LectureResponseDto.LectureDeleteResponse> deactivateAvailableTimes(
            @RequestBody @Valid LectureRequestDto.LectureDeleteRequest req){
        Long tutorId= req.getTutorId();
        // -- 사용자 validate 로직 추가 되어야 함 -- //
        return ApiResponse.onSuccess(lectureService.deleteLecture(req));
    }
}
