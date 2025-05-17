package neordinary.backend.nteam.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class BaseErrorResponse {
    private String code;
    private String message;

    public static ResponseEntity<BaseErrorResponse> get(ErrorStatus code) {
        BaseErrorResponse res = new BaseErrorResponse(code.getCode(), code.getMessage());
        return new ResponseEntity<>(res, code.getHttpStatus());
    }
}
