package com.theocean.fundering.domain.withdrawal.service;

import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import com.theocean.fundering.domain.withdrawal.dto.WithdrawalRequest;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;

    // 출금 신청 저장 로직
    public void applyWithdrawal(final Long memberId, final Long postId, final WithdrawalRequest.SaveDTO request) {
        final Withdrawal withdrawal = Withdrawal.builder()
                .applicantId(memberId)
                .postId(postId)
                .usage(request.getUsage())
                .depositAccount(request.getDepositAccount())
                .withdrawalAmount(request.getAmount())
                .build();

        withdrawalRepository.save(withdrawal);
    }
}
