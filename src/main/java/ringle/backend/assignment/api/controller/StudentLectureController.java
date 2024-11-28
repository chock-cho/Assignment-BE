package ringle.backend.assignment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.api.dto.ResponseDto.TempResponse;
import ringle.backend.assignment.aspect.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.converter.TempConverter;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.service.LectureService.LectureService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "${swagger.tag.student_lecture}")
public class StudentLectureController {
    private final LectureService lectureService;

    @GetMapping("/available-tutors")
    @Operation(summary = "수업 가능한 튜터 조회 API",
            description = "수업 가능한 튜터 조회 API - 시간대 & 수업 길이로 수업 가능한 튜터 조회")
    public ApiResponse<List<LectureResponseDto.LectureGetResponse>> getAvailableTutorLectures(
            @RequestParam TimeSlot timeSlot,
            @RequestParam LectureType lectureType) {
        return ApiResponse.onSuccess(lectureService.getAvailableTutorsByTimeSlotAndLength(timeSlot, lectureType));
    }
}
