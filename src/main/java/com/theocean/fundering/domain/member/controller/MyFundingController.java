package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.MyFundingFollowingCelebsDTO;
import com.theocean.fundering.domain.member.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.member.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.member.service.MyFundingService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MYFUNDING", description = "My 펀딩 관련 API")
@RequiredArgsConstructor
@RestController
public class MyFundingController {
    private final MyFundingService myFundingService;

    @Operation(summary = "주최한 펀딩 목록 조회", description = "사용자의 토큰으로 주최한 펀딩 목록을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MyFundingHostResponseDTO.class))),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/host")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPostingByHost(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(hidden = true) @PageableDefault final Pageable pageable
    ){
        final var page = myFundingService.findAllPostingByHost(userDetails.getId(), pageable);
        return ApiResult.success(page);
    }

    @Operation(summary = "후원한 펀딩 목록 조회", description = "사용자의 토큰으로 후원한 펀딩 목록을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MyFundingSupporterResponseDTO.class)))
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/support")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPostingBySupport(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(hidden = true) @PageableDefault final Pageable pageable
    ){
        final var page = myFundingService.findAllPostingBySupporter(userDetails.getId(), pageable);
        return ApiResult.success(page);
    }

    @Operation(summary = "닉네임 조회", description = "사용자의 토큰으로 닉네임을 조회한다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> getNickname(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        final String nickname = myFundingService.getNickname(userDetails.getId());
        return ApiResult.success(nickname);
    }

    @Operation(summary = "팔로잉 한 셀럽 조회", description = "사용자의 토큰으로 팔로잉 한 셀럽을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MyFundingFollowingCelebsDTO.class)))
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/followers")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findFollowingCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        final var followingCelebs = myFundingService.findFollowingCelebs(userDetails.getId());
        return ApiResult.success(followingCelebs);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAwaitingApprovalWithdrawals(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            final Pageable pageable
    ){
        final var page = myFundingService.findAwaitingApprovalWithdrawals(userDetails.getId(), pageable);
        return ApiResult.success(page);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/myfunding/withdrawal/approval")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?>  approvalWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @RequestParam final Long withdrawalId
    ) {
        myFundingService.approvalWithdrawal(userDetails.getId(), postId, withdrawalId);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/myfunding/withdrawal/rejection   ")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?>  rejectWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @RequestParam final Long withdrawalId
    ) {
        myFundingService.rejectWithdrawal(userDetails.getId(), postId, withdrawalId);
        return ApiResult.success(null);
    }
}