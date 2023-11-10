package com.theocean.fundering.global.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LoginRequest {
    @Getter
    @RequiredArgsConstructor
    public static class RefreshTokenDTO{
        private String refreshToken;
    }
}
