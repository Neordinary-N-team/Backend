package neordinary.backend.nteam.global.apiPayload.code;

import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;

public interface BaseErrorCode {
    ErrorReasonDTO getReason();
    ErrorReasonDTO getReasonHttpStatus();
}
