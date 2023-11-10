package com.theocean.fundering.global.jwt.handler;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception401;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.jwt.dto.LoginRequest;
import com.theocean.fundering.global.jwt.dto.LoginResponse;
import com.theocean.fundering.global.jwt.service.LoginService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RefreshController {
    private final LoginService loginService;

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> checkRefreshAccessToken(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody final LoginRequest.RefreshTokenDTO refreshTokenDTO ){
        final var accessTokenDTO = loginService.checkRefreshToken(refreshTokenDTO.getRefreshToken(), userDetails.getEmail());
        return ApiResult.success(accessTokenDTO);
    }
}
