package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.service.FollowService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FOLLOW", description = "celeb 팔로우 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "셀럽 팔로우", description = "셀럽의 아이디를 기반으로 팔로우한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/follow")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> followCelebs(
            @Parameter(description = "셀럽의 PK") @PathVariable("celebId") Long celebId,
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        followService.followCelebs(celebId, userDetails.getId());
        return ApiResult.success(null);
    }

    @Operation(summary = "셀럽 팔로우 취소", description = "셀럽의 아이디를 기반으로 팔로우를 취소한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> unfollowCelebs(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId,
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        followService.unFollowCelebs(celebId, userDetails.getId());
        return ApiResult.success(null);
    }

}