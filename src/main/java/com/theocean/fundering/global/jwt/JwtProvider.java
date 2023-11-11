package com.theocean.fundering.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Getter
    @Value("${jwt.access-secret}")
    private String ACCESS_SECRET;
    @Getter
    @Value("${jwt.refresh-secret}")
    private String REFRESH_SECRET;
    private static Long ACCESS_EXP = 7200000L;
    private static Long REFRESH_EXP = 1209600000L;
    private static final String ACCESS_HEADER = "Authorization";
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";

    private static final String EMAIL_CLAIM = "email";
    private static final String TOKEN_PREFIX = "Bearer ";

    public String createAccessToken(final String email) {
        final String jwt = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public String createRefreshToken(final String email) {
        final String jwt = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(REFRESH_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public Optional<String> extractAccessToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(accessToken -> accessToken.startsWith(TOKEN_PREFIX))
                .map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public void sendAccess(final HttpServletResponse response, final String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(ACCESS_HEADER, accessToken);
    }


    public boolean isAccessTokenValid(final String token) {
        try {
            JWT.require(Algorithm.HMAC512(ACCESS_SECRET)).build().verify(token);
            return true;
        } catch (final RuntimeException e) {
            log.error("유효하지 않은 토큰입니다. {}", new String[]{e.getMessage()});
            return false;
        }
    }

    public Optional<String> verifyAccessTokenAndExtractEmail(final String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                    .build()
                    .verify(accessToken) // accessToken 검증
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (final RuntimeException e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }
}
