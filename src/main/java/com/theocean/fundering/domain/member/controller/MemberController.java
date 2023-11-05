package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup/check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> checkEmail(@RequestBody @Valid final EmailRequestDTO emailRequestDTO, final Error error) {
        memberService.sameCheckEmail(emailRequestDTO.getEmail());
        return ApiResult.success(null);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> signUp(@RequestBody @Valid final MemberSignUpRequestDTO requestDTO, Error error) {
        memberService.signUp(requestDTO);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/setting")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllUserSetting(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        var responseDTO = memberService.findAllUserSetting(userDetails.getId());
        return ApiResult.success(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user/setting")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> updateUserSetting(
            @RequestBody @Valid final MemberSettingRequestDTO requestDTO, Error error,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        memberService.updateUserSetting(requestDTO, userDetails.getId(), thumbnail);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/setting/cancellation")

    public ApiResult<?> cancellationUser(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        memberService.cancellationUser(userDetails.getId());
        ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> member() {
        return ApiResult.success(null);
    }

}
