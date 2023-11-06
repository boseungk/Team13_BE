package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.service.FollowService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {
    private final FollowService followService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/follow")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> followCelebs(@PathVariable final Long celebId,
                                          @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        followService.followCelebs(celebId, userDetails.getId());
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/celebs/{celebId}/unfollow")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> unfollowCelebs(@PathVariable final Long celebId,
                                            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        followService.unFollowCelebs(celebId, userDetails.getId());
        return ApiResult.success(null);
    }

}
