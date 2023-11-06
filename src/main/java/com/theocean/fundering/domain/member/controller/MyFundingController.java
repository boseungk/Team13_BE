package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.service.MyFundingService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MyFundingController {
    private final MyFundingService myFundingService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/host")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPostingByHost(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PageableDefault final Pageable pageable) {
        final var page = myFundingService.findAllPostingByHost(userDetails.getId(), pageable);
        return ApiResult.success(page);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/support")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPostingBySupport(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PageableDefault final Pageable pageable
    ) {
        final var page = myFundingService.findAllPostingBySupporter(userDetails.getId(), pageable);
        return ApiResult.success(page);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> getNickname(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        final String nickname = myFundingService.getNickname(userDetails.getId());
        return ApiResult.success(nickname);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/followers")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findFollowingCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        final var followingCelebs = myFundingService.findFollowingCelebs(userDetails.getId());
        return ApiResult.success(followingCelebs);
    }


    @PostMapping("/myfunding/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?>  approvalWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId
    ) {
        myFundingService.applyWithdrawal(userDetails.getId(), postId);
        return ApiResult.success(null);
    }
}