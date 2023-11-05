package com.theocean.fundering.domain.account.dto;

import lombok.Getter;

@Getter
public class BalanceResponse {
    private final int balance;

    public BalanceResponse(final int balance) {
        this.balance = balance;
    }
}
