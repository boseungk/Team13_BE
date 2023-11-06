package com.theocean.fundering.global.errors.exception;

import com.theocean.fundering.global.utils.ApiResult;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception403 extends RuntimeException {
    public Exception403(final String message) {
        super(message);
    }

    public ApiResult<?> body() {
        return ApiResult.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}