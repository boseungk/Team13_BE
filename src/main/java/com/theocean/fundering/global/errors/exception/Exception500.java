package com.theocean.fundering.global.errors.exception;

import com.theocean.fundering.global.utils.ApiResult;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서버 에러
@Getter
public class Exception500 extends RuntimeException {
    public Exception500(final String message) {
        super(message);
    }

    public ApiResult<?> body() {
        return ApiResult.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

