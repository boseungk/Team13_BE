package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.EmailRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberRequestDTO;
import com.theocean.fundering.domain.member.service.MemberService;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult<?> signUp(@RequestBody @Valid final MemberRequestDTO requestDTO, final Error error) {
        memberService.signUp(requestDTO);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> member() {
        return ApiResult.success(null);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid MemberRequestDTO requestDTO, Error error){
//        memberService.login(requestDTO);
//    }
}