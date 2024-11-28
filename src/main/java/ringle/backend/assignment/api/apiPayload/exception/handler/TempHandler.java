package ringle.backend.assignment.api.apiPayload.exception.handler;

import ringle.backend.assignment.api.apiPayload.code.BaseErrorCode;
import ringle.backend.assignment.api.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
