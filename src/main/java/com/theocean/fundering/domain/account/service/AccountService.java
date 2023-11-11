package com.theocean.fundering.domain.account.service;

import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public int getBalance(final long postId) {
        final Account account = accountRepository.findByPostId(postId).orElseThrow(() -> new Exception404(ErrorCode.ER03));

        return account.getBalance();
    }
}
