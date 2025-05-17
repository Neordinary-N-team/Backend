package neordinary.backend.nteam.global.exception;

import jakarta.validation.ConstraintViolationException;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getReason());
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getConstraintViolations().iterator().next().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getParameterName() + " 파라미터는 필수 입력 값입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getName() + " 파라미터의 형식이 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "요청 본문의 형식이 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorStatus errorStatus = ErrorStatus._BAD_REQUEST;
        BaseErrorResponse res = new BaseErrorResponse(errorStatus.getCode(), errorMessage);
        return new ResponseEntity<>(res, errorStatus.getHttpStatus());
    }

    @ExceptionHandler(MemberHandler.class)
    public ResponseEntity<BaseErrorResponse> handleMemberException(MemberHandler ex) {
        return BaseErrorResponse.get(ex.getErrorStatus());
    }

    @ExceptionHandler(DietHandler.class)
    public ResponseEntity<BaseErrorResponse> handleDietException(DietHandler ex) {
        return BaseErrorResponse.get(ex.getErrorStatus());
    }
  
    @ExceptionHandler(DiaryHandler.class)
    public ResponseEntity<BaseErrorResponse> handleDiaryException(DiaryHandler ex) {
        return BaseErrorResponse.get(ex.getErrorStatus());
    }

}