package com.theocean.fundering.domain.withdrawal.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(nullable = false)
    private int withdrawalAmount;

    // 승인 여부
    @Column(nullable = false)
    private Boolean isApproved;

    // 생성자
    @Builder
    public Withdrawal(final Long applicantId, final Long postId, final String usage, final String depositAccount, final int withdrawalAmount) {
        this.applicantId = applicantId;
        this.postId = postId;
        this.usage = usage;
        this.depositAccount = depositAccount;
        this.withdrawalAmount = withdrawalAmount;
        isApproved = false;
    }

    public void approveWithdrawal(){
        isApproved = true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Withdrawal that)) return false;
        return Objects.equals(withdrawalId, that.withdrawalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(withdrawalId);
    }
}
