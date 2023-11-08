package com.theocean.fundering.domain.withdrawal.controller;

import com.theocean.fundering.domain.withdrawal.dto.WithdrawalRequest;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalResponse;
import com.theocean.fundering.domain.withdrawal.service.WithdrawalService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WITHDRAWAL", description = "출금 관련 API")
@RestController
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    // (기능) 출금 신청하기
    @Operation(summary = "펀딩 출금 신청", description = "펀딩 id(게시글 id)를 기반으로 출금 신청을 한다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> applyWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid @Schema(implementation = WithdrawalRequest.SaveDTO.class) final WithdrawalRequest.SaveDTO request,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId) {

        final Long memberId = userDetails.getId();
        withdrawalService.applyWithdrawal(memberId, postId, request);

        return ApiResult.success(null);
    }

    // (기능) 출금내역 조회
    @Operation(summary = "출금내역 조회", description = "펀딩 id(게시글 id)를 기반으로 남은 금액과 출금 내역을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = WithdrawalResponse.FindAllDTO.class)))
    })
    @GetMapping("/posts/{postId}/withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<WithdrawalResponse.FindAllDTO> readWithdrawals(
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(hidden = true) @PageableDefault(size = 10) final Pageable pageable
    ){
        final var response = withdrawalService.getWithdrawals(postId, pageable);

        return ApiResult.success(response);
    }
}
