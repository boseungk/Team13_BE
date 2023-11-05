package com.theocean.fundering.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.oauth2.CustomOAuth2User;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final String SUCCESS_MESSAGE = "로그인에 성공했습니다.";

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2Login Success");
        try {
            final CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, oAuth2User);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            final String result = objectMapper.writeValueAsString(ApiResult.success(SUCCESS_MESSAGE));
            response.getWriter().write(result);
        } catch (final Exception e) {
            throw e;
        }
    }

    private void loginSuccess(final HttpServletResponse response, final CustomOAuth2User oAuth2User) throws IOException {
        final String accessToken = jwtProvider.createAccessToken(oAuth2User.getEmail());
        final String refreshToken = jwtProvider.createRefreshToken(oAuth2User.getEmail());
        response.setHeader(JwtProvider.ACCESS_HEADER, accessToken);
        response.setHeader(JwtProvider.REFRESH_HEADER, refreshToken);

        jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }
}
