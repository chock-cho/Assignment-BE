package ringle.backend.assignment.aspect.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ringle.backend.assignment.aspect.apiPayload.code.BaseErrorCode;
import ringle.backend.assignment.aspect.apiPayload.code.ErrorReasonDto;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
