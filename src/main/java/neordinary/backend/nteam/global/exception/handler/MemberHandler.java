package neordinary.backend.nteam.global.exception.handler;

import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;

public class MemberHandler extends RuntimeException {
    private final ErrorStatus errorStatus;

    public MemberHandler(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}