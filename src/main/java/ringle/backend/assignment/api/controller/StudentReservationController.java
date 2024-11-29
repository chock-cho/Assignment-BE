package ringle.backend.assignment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.aspect.annotation.ValidateReservation;
import ringle.backend.assignment.aspect.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.service.ReservationService.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "${swagger.tag.student_reservation}")
public class StudentReservationController {
    private final ReservationService reservationService;
    @PostMapping("/reservation")
    @ValidateReservation
    @Operation(summary = "수업 신청 API - 학생이 튜터가 오픈한 수업을 신청합니다.",
            description = """
                     학생이 **오픈한 수업을 신청.**합니다. \s
                     *  ️️수업은 총 30분, 60분 수업이 있습니다.\s
                     * 30분 수업 신청 시, 첫 교시(startLecture)에 해당하는 수업과 마지막 교시(endLecture)에 해당하는 수업의 id를 동일하게 입력합니다. \s
                     * 60분 수업 신청 시, 첫 교시(startLecture)에 해당하는 수업과 마지막 교시(endLecture)에 해당하는 수업의 id를 다르게 입력합니다. \s
                     \s
                     [RequestParam] \s
                     - studentId : 신청을 시도하는 학생의 id(Long) \s
                     - 해당 studentId는 인증, 인가 로직 구현 후 Header의 Authorization에 추가 될 예정입니다. \s
                     \s
                     [Body]\s
                     - ⚠️ tutorId : 신청하려는 수업의 튜터 id(Long)\s
                     - ⚠️ startLectureId : 신청하려는 수업의 1교시 수업 id(Long)" \s
                     - ⚠️ endLectureId : 신청하려는 수업의 2교시 수업 id(Long)" \s
                    \s""")
    public ApiResponse<ReservationResponseDto.ReservationCreateResponse> createReservation(
            @RequestParam Long studentId,
            @RequestBody @Valid ReservationRequestDto.ReservationCreateRequest req){
        return ApiResponse.onSuccess(reservationService.makeReservation(studentId, req));
    }
}
