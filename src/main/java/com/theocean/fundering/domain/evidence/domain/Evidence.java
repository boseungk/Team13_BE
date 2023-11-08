package com.theocean.fundering.domain.evidence.domain;


import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evidence", indexes = @Index(name = "index_withdrawal", columnList = "withdrawal_id", unique = true))
@Entity
public class Evidence extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long evidenceId;

    @Column(nullable = false, name = "withdrawal_id")
    private Long withdrawalId;

    @Column(nullable = false, name = "applicant_id")
    private Long applicantId;

    @Column(nullable = false, name = "post_id")
    private Long postId;

    @Column(nullable = false, name = "url")
    private String url;

    @Builder
    public Evidence(final Long withdrawalId, final Long applicantId, final Long postId, final String url) {
        this.withdrawalId = withdrawalId;
        this.applicantId = applicantId;
        this.postId = postId;
        this.url = url;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Evidence evidence)) return false;
        return Objects.equals(evidenceId, evidence.evidenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId);
    }
}
