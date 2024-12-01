package ringle.backend.assignment.api.controller.LectureController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ringle.backend.assignment.api.dto.ResponseDto.LectureResponseDto;
import ringle.backend.assignment.aspect.annotation.ValidateLecture;
import ringle.backend.assignment.aspect.annotation.ValidateReservation;
import ringle.backend.assignment.aspect.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.service.LectureService.LectureQueryService;
import ringle.backend.assignment.service.LectureService.LectureService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "${swagger.tag.student_lecture}")
public class StudentLectureController {
    private final LectureQueryService lectureQueryService; //GET 메서드
    @GetMapping("/available-tutors")
    @ValidateReservation
    @Operation(summary = " ️STEP 3️⃣- 수업 가능한 튜터 목록 조회 API",
            description = """
                     예약 Flow 중, 학생이 **날짜 & 수업 시작 시각 & 수업 길이 로 현재 가능한 수업 목록을 조회**합니다. \s
                     신청 가능한 수업 길이는 30분, 60분 단위가 있습니다. \s
                     \s
                     - ⚠️ `date` : 조회하려는 수업의 날짜(String) - "YYYY-MM-DD"\s
                     - ⚠️ `timeSlot` : 조회하려는 수업 시작 시각(enum) -30분 단위" \s
                     - ⚠️ `LectureType`(30분, 60분): `_30_MIN` | `_60_MIN`\s
                     \s
                    **[Test 예시]**
                    ```
                    - date: 2024-12-02\s
                    - timeSlot : SLOT_11_00\s
                    - lectureType : _30_MIN\s
                    ```
                    \s""")
    public ApiResponse<List<LectureResponseDto.LectureGetResponse>> getAvailableTutorLectures(
            @RequestParam LocalDate date,
            @RequestParam TimeSlot timeSlot,
            @RequestParam LectureType lectureType) {
        return ApiResponse.onSuccess(lectureQueryService.getAvailableTutors(date, timeSlot, lectureType));
    }

    @GetMapping("/available-lectures")
    @ValidateReservation
    @Operation(summary = "STEP 4️⃣ - 가능한 수업 목록 조회 API",
            description = """
                     예약 Flow 중, 학생이 **수업 가능 기간 & 수업 길이 로 현재 가능한 수업 목록을 조회**합니다. \s
                     신청 가능한 수업 길이는 30분, 60분 단위가 있습니다. \s
                     \s
                     - ⚠️ `startDate`: 조회하려는 기간의 시작 날짜(String) - "YYYY-MM-DD" \s
                     - ⚠️ `endDate`: 조회하려는 기간의 종료 날짜(String) - "YYYY-MM-DD" \s
                     - ⚠️ `LectureType`(30분, 60분): _30_MIN | _60_MIN \s
                     \s
                     **[Test 예시]**
                    ```
                    - startDate: 2024-12-02\s
                    - endDate : 2024-12-03\s
                    - lectureType : _30_MIN\s
                    ```
                    \s""")
    public ApiResponse<List<LectureResponseDto.LecturesGetResponseForTutor>> getAvailableLectures(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam LectureType lectureType){
        return ApiResponse.onSuccess(lectureQueryService.getAvailableLectures(startDate, endDate, lectureType));
    }
}
