package com.theocean.fundering.global.errors;

import com.theocean.fundering.global.errors.exception.*;
import com.theocean.fundering.global.utils.ApiResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(final Exception400 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(final Exception401 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(final Exception403 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> notFound(final Exception404 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> serverError(final Exception500 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 필드 에러들 중에서 첫 번째 에러의 기본 메시지만 가져옵니다.
        final String errorMessage = fieldErrors.stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        return ApiResult.error(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        return ApiResult.error("잘못된 요청값입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<?> accessDeniedError(final AccessDeniedException e) {
        return ApiResult.error("접근 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> unknownServerError(final Exception e) {
        return ApiResult.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
