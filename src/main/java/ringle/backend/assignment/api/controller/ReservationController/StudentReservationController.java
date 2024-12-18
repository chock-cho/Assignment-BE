package ringle.backend.assignment.api.controller.ReservationController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.aspect.annotation.ValidateReservation;
import ringle.backend.assignment.aspect.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.service.ReservationService.ReservationQueryService;
import ringle.backend.assignment.service.ReservationService.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "${swagger.tag.student_reservation}")
public class StudentReservationController {
    private final ReservationService reservationService;
    private final ReservationQueryService reservationQueryService;
    @PostMapping("/reservation")
    @Operation(summary = "STEP 5️⃣ - 수업 신청 API",
            description = """
                     예약 Flow 중, 학생이 **오픈한 수업을 신청**합니다. \s
                     *  ️️수업은 총 30분, 60분 수업이 있습니다.\s
                     * 30분 수업 신청 시, 첫 교시(startLecture)에 해당하는 수업과 마지막 교시(endLecture)에 해당하는 수업의 id를 동일하게 입력합니다. \s
                     * 60분 수업 신청 시, 첫 교시(startLecture)에 해당하는 수업과 마지막 교시(endLecture)에 해당하는 수업의 id를 다르게 입력합니다. \s
                     \s
                     [RequestParam] \s
                     - studentId : 신청을 시도하는 학생의 id(Long) \s
                     - 해당 studentId는 인증, 인가 로직 구현 후 Header의 Authorization에 추가 될 예정입니다. \s
                     \s
                     [RequestBody]\s
                     - ⚠️ `tutorId` : 신청하려는 수업의 튜터 id(Long)\s
                     - ⚠️ `startLectureId` : 신청하려는 수업의 1교시 수업 id(Long)- 조회 API에서 확인 가능" \s
                     - ⚠️ `endLectureId` : 신청하려는 수업의 2교시 수업 id(Long)- 조회 API에서 확인 가능" \s
                    **[Test 예시]**
                    ```
                    - studentId: 1\s
                    - startLectureId : 3\s
                    - lectureType : 4\s
                    ```
                    \s""")
    public ApiResponse<ReservationResponseDto.ReservationCreateResponse> createReservation(
            @RequestParam Long studentId,
            @RequestBody @Valid ReservationRequestDto.ReservationCreateRequest req){
        return ApiResponse.onSuccess(reservationService.makeReservation(studentId, req));
    }

    @GetMapping("/myReservation")
    @Operation(summary = "STEP ️6️⃣ - 신청한 수업 목록 조회 API",
            description = """
                     학생이 **신청한 수업 목록을 조회**합니다. \s
                     *  ️예약한 ️수업은 총 30분, 60분 수업이 있습니다.\s
                     * 30분 수업 정보 반환 시, 반환되는 lectureInfo에 첫 교시(startLecture)에 해당하는 수업의 정보값만 반환됩니다. \s
                     * 60분 수업 정보 반환 시, 반환되는 lectureInfo에 첫 교시(startLecture)와 마지막 교시(endLecture)에 해당하는 수업의 정보값이 반환됩니다. \s
                     \s
                     [RequestParam] \s
                     - studentId : 신청을 시도하는 학생의 id(Long) \s
                     - 해당 studentId는 인증, 인가 로직 구현 후 Header의 Authorization에 추가 될 예정입니다. \s
                     \s
                     [ResponseBody] \s
                     - reservationId : 신청한 수업 내역의 예약 id(Long) \s
                     - reservedLectureType: 신청한 수업 내역의 수업 타입(String) \s
                     - tutorInfo: 신청한 수업 내역의 튜터 정보(tutorId, tutorName) \s
                     - lectureInfo: 신청한 수업 내역의 교시별 수업 정보(\s
                     startLecture(lectureId, lectureTimeSlot), endLecture(lectureId, lectureTimeSlot)
                     ) \s
                    **[Test 예시]**
                    ```
                    - studentId: 1\s
                    ```
                    \s""")
    public ApiResponse<List<ReservationResponseDto.ReservationInfoResponse>> getReservations(
            @RequestParam Long studentId){
        return ApiResponse.onSuccess(reservationQueryService.getMyReservations(studentId));
    }
}
