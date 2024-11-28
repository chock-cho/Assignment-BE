package ringle.backend.assignment.aspect.apiPayload.code;

public interface BaseErrorCode {

    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
