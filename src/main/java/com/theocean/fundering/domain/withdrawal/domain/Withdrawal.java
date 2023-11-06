package com.theocean.fundering.domain.withdrawal.domain;

import com.theocean.fundering.global.utils.ApprovalStatus;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.util.Objects;
import java.util.prefs.AbstractPreferences;

@Entity
@Table(name = "Withdrawal")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawal extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalId;

    @Column(nullable = false)
    private Long applicantId;

    @Column(nullable = false)
    private Long postId;

    // 사용처
    @Column(nullable = false)
    private String usage;

    // 입금계좌
    @Column(nullable = false)
    private String depositAccount;

    // 출금액
    @Min(0)
    @Column(nullable = false)
    private int withdrawalAmount;

    // 승인 여부
    @Column(nullable = false)
    private ApprovalStatus status;

    // 출금시 계좌 잔액
    @Min(0)
    @Column
    private Integer balance;

    // 생성자
    @Builder
    public Withdrawal(final Long applicantId, final Long postId, final String usage, final String depositAccount, final int withdrawalAmount) {
        this.applicantId = applicantId;
        this.postId = postId;
        this.usage = usage;
        this.depositAccount = depositAccount;
        this.withdrawalAmount = withdrawalAmount;
        status = ApprovalStatus.PENDING;
    }

    public long getDepositTime() {
        return modifiedAt.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public void approveWithdrawal() {
        status = ApprovalStatus.APPROVED;
    }

    public void denialWithdrawal(){
        status = ApprovalStatus.PENDING;
    }

    public void updateBalance(final int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Withdrawal withdrawal)) return false;
        return Objects.equals(withdrawalId, withdrawal.withdrawalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(withdrawalId);
    }
}
