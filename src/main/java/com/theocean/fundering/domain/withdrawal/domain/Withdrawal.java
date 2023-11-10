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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "withdrawal")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawal extends AuditingFields {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalId;

    @Column(nullable = false, name = "applicant_id")
    private Long applicantId;

    @Column(nullable = false, name = "post_id")
    private Long postId;

    // 사용처
    @Column(nullable = false, name = "purpose")
    private String purpose;

    // 입금계좌
    @Column(nullable = false, name = "depositAccount")
    private String depositAccount;

    // 출금액
    @Min(0)
    @Column(nullable = false, name = "withdrawal_amount")
    private int withdrawalAmount;

    // 승인 여부
    @Column(nullable = false, name = "approval_status")
    private ApprovalStatus status;

    // 출금시 계좌 잔액
    @Min(0)
    @Column(name = "balance")
    private Integer balance;

    // 생성자
    @Builder
    public Withdrawal(final Long applicantId, final Long postId, final String usage, final String depositAccount, final int withdrawalAmount) {
        this.applicantId = applicantId;
        this.postId = postId;
        this.purpose = usage;
        this.depositAccount = depositAccount;
        this.withdrawalAmount = withdrawalAmount;
        status = ApprovalStatus.PENDING;
    }

    public void approveWithdrawal(int balance) {
        status = ApprovalStatus.APPROVED;
        this.balance = balance;
    }

    public void denyWithdrawal(){
        status = ApprovalStatus.PENDING;
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
