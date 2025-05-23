package neordinary.backend.nteam.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import neordinary.backend.nteam.global.apiPayload.code.BaseCode;
import neordinary.backend.nteam.global.apiPayload.code.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "리소스가 성공적으로 생성되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}