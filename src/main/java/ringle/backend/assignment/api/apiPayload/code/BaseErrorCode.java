package ringle.backend.assignment.api.apiPayload.code;

public interface BaseErrorCode {

    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
