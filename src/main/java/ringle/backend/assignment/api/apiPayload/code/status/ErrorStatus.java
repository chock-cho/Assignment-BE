package ringle.backend.assignment.api.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ringle.backend.assignment.api.apiPayload.code.BaseErrorCode;
import ringle.backend.assignment.api.apiPayload.code.ErrorReasonDto;
import ringle.backend.assignment.domain.Lecture;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // TEST용 커스텀 exception
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // Tutor 수업 등록 관련 exception
    TUTOR_NOT_FOUND(HttpStatus.BAD_REQUEST, "TUTOR4001", "존재하지 않는 튜터입니다"),
    TUTOR_FORBIDDEN(HttpStatus.BAD_REQUEST, "TUTOR4002", "튜터 본인이 등록한 수업만 삭제할 수 있습니다."),

    // Lecture 관련 exception
    LECTURE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LECTURE4001", "존재하지 않는 수업입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }
}
