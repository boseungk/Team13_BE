package com.theocean.fundering.domain.heart.controller;


import com.theocean.fundering.domain.heart.service.HeartService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> addHeart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable Long postId){
        heartService.addHeart(userDetails.getId(), postId);
        return ApiResult.success(null);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/posts/{postId}/unHeart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> unHeart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long postId){

        heartService.unHeart(userDetails.getId(), postId);
        return ApiResult.success(null);
    }
}
