package neordinary.backend.nteam.global.exception;

import neordinary.backend.nteam.global.apiPayload.code.BaseErrorCode;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import org.hibernate.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler({NoHandlerFoundException.class, TypeMismatchException.class})
    public ResponseEntity<BaseErrorResponse> handle_BadRequest(Exception e) {
        //log.error("[BaseExceptionHandler: handle_BadRequest 호출]");
        return BaseErrorResponse.get(ErrorStatus._BAD_REQUEST);
    }
}