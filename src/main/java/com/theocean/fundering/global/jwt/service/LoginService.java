package com.theocean.fundering.global.jwt.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception401;
import com.theocean.fundering.global.jwt.JwtProvider;
import com.theocean.fundering.global.jwt.dto.LoginResponse;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return CustomUserDetails.from(memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."))
        );
    }

    @Transactional
    public LoginResponse.AccessTokenDTO checkRefreshToken(final String refreshToken, final String email){
        final Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
        final String newAccessToken = jwtProvider.createAccessToken(email);
        final String newRefreshToken = jwtProvider.createRefreshToken(email);
        memberRepository.save(member.updateRefreshToken(newRefreshToken));
        return LoginResponse.AccessTokenDTO.from(newAccessToken);
    }
}
