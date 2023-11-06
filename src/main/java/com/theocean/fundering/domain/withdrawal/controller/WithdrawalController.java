package com.theocean.fundering.domain.withdrawal.controller;

import com.theocean.fundering.domain.withdrawal.dto.WithdrawalRequest;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalResponse;
import com.theocean.fundering.domain.withdrawal.service.WithdrawalService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
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

@RestController
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    // (기능) 출금 신청하기
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> applyWithdrawal(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final WithdrawalRequest.SaveDTO request,
            @PathVariable final long postId) {

        final Long memberId = userDetails.getId();
        withdrawalService.applyWithdrawal(memberId, postId, request);

        return ApiResult.success(null);
    }

    // (기능) 출금내역 조회
    @GetMapping("/posts/{postId}/withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<WithdrawalResponse.FindAllDTO> readWithdrawals(@PathVariable final long postId, @PageableDefault(size = 10) final Pageable pageable) {

        final var response = withdrawalService.getWithdrawals(postId, pageable);

        return ApiResult.success(response);
    }
}
