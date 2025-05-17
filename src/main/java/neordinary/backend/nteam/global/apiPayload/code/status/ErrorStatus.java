package neordinary.backend.nteam.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import neordinary.backend.nteam.global.apiPayload.code.BaseErrorCode;
import neordinary.backend.nteam.global.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "사용자가 없습니다."),
    CREATE_MEMBER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER5001", "회원 생성에 실패했습니다."),

    // 식단 관련 에러
    DIET_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIET4001", "식단이 없습니다."),
    PERIOD_TOO_LONG(HttpStatus.BAD_REQUEST, "DIET4002", "기간 설정 기준을 초과하였습니다."),

    // 일기 관련 에러
    START_DATE_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "DIARY4002", "시작일이 종료일보다 늦을 수 없습니다."),
    BAD_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "DIARY4001", "이미지 파일만 업로드 가능합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}