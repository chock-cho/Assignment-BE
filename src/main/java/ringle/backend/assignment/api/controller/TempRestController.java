package ringle.backend.assignment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ringle.backend.assignment.api.apiPayload.exception.ApiResponse;
import ringle.backend.assignment.api.dto.ResponseDto.TempResponse;
import ringle.backend.assignment.converter.TempConverter;
import ringle.backend.assignment.service.TempService.TempQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Tag(name = "${swagger.tag.test}")
public class TempRestController {

    private final TempQueryService tempQueryService;
    @GetMapping("/health")
    @Operation(summary = "health check API",
            description = "서버 상태 확인 API - 정상적으로 동작 중인지 확인")
    public ApiResponse<TempResponse.TempTestDTO> testAPI() {
        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    @GetMapping("/exception")
    @Operation(summary = "예외처리 테스트",
            description = "flag 값이 1일 경우 예외 발생")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempQueryService.checkFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }
}
