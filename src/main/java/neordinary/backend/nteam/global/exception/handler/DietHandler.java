package neordinary.backend.nteam.global.exception.handler;


import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;

public class DietHandler extends RuntimeException {
    private final ErrorStatus errorStatus;

    public DietHandler(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public DietHandler(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
