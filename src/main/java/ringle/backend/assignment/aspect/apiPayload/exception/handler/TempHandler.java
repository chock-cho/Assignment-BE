package ringle.backend.assignment.aspect.apiPayload.exception.handler;

import ringle.backend.assignment.aspect.apiPayload.code.BaseErrorCode;
import ringle.backend.assignment.aspect.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
