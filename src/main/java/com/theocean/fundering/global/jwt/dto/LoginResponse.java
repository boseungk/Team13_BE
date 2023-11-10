package com.theocean.fundering.global.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LoginResponse {
    @Getter
    @RequiredArgsConstructor
    public static class SuccessDTO{
        private final String profileImage;
        private final String nickname;
        private final boolean isCoAdmin;
        private final String refreshToken;

        public static LoginResponse.SuccessDTO of(final String profileImg, final String nickname, final boolean isCoAdmin, final String refreshToken){
            return new SuccessDTO(profileImg, nickname, isCoAdmin, refreshToken);
        }
    }
    @Getter
    @RequiredArgsConstructor
    public static class RefreshTokenDTO {
        private final String refreshToken;
        public static RefreshTokenDTO from(final String refreshToken){
            return new RefreshTokenDTO(refreshToken);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class AccessTokenDTO{
        private final String accessToken;
        public static AccessTokenDTO from(final String accessToken){
            return new AccessTokenDTO(accessToken);
        }
    }
}
