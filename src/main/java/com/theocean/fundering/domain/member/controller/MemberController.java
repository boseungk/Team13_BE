package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import com.theocean.fundering.global.utils.ApiUtils;
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
>>>>>>>feat

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
<<<<<<<HEAD

    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberSignUpRequestDTO requestDTO, Error error) {
=======
        @ResponseStatus(HttpStatus.OK)
        public ApiResult<?> signUp ( @RequestBody @Valid final MemberRequestDTO requestDTO, final Error error){
>>>>>>>feat
            memberService.signUp(requestDTO);
            return ApiResult.success(null);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/user/setting")
        public ResponseEntity<?> findAllUserSetting (
        @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
            var responseDTO = memberService.findAllUserSetting(userDetails.getId());
            return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @PutMapping("/user/setting")
        public ResponseEntity<?> modifyUserSetting (
        @RequestBody @Valid final MemberSettingRequestDTO requestDTO, Error error,
        @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
            memberService.modifyUserSetting(requestDTO, userDetails.getId());
            return ResponseEntity.ok().body(ApiUtils.success("성공적으로 변경되었습니다."));
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @DeleteMapping("/user/setting/cancellation")
        public ResponseEntity<?> cancellationUser (
        @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
            memberService.cancellationUser(userDetails.getId());
            return ResponseEntity.ok().body(ApiUtils.success("회원 탈퇴 되었습니다."));
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/member")
        @ResponseStatus(HttpStatus.OK)
        public ApiResult<?> member () {
            return ApiResult.success(null);
        }

    }