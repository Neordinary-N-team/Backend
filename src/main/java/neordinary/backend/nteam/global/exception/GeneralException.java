package neordinary.backend.nteam.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import neordinary.backend.nteam.global.apiPayload.code.BaseErrorCode;
import neordinary.backend.nteam.global.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}