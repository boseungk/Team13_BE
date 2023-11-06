package com.theocean.fundering.domain.account.controller;

import com.theocean.fundering.domain.account.dto.BalanceResponse;
import com.theocean.fundering.domain.account.service.AccountService;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // (기능) 펀딩 출금가능 금액 조회
    @GetMapping("/posts/{postId}/balance")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<BalanceResponse> getFundingBalance(@PathVariable final long postId) {

        final int balance = accountService.getBalance(postId);
        final var balanceResponse = new BalanceResponse(balance);

        return ApiResult.success(balanceResponse);
    }
}
