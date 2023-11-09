package com.theocean.fundering.domain.heart.controller;


import com.theocean.fundering.domain.heart.service.HeartService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "HEART", description = "찜하기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "찜하기", description = "펀딩 id를 기반으로 펀딩을 찜한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> addHeart(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                 @PathVariable final Long postId) {
        heartService.addHeart(userDetails.getId(), postId);
        return ApiResult.success(null);

    }

    @Operation(summary = "찜하기 취소", description = "펀딩 id를 기반으로 펀딩 찜을 취소한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/posts/{postId}/unHeart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> unHeart(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                @PathVariable final Long postId) {

        heartService.unHeart(userDetails.getId(), postId);
        return ApiResult.success(null);
    }
}