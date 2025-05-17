package neordinary.backend.nteam.global.exception.handler;

import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DiaryHandler extends RuntimeException {
    private final ErrorStatus errorStatus;

    public DiaryHandler(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
