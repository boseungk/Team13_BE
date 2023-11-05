package com.theocean.fundering.domain.evidence.domain;


import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evidence", indexes = @Index(name = "index_withdrawal", columnList = "withdrawalId", unique = true))
@Entity
public class Evidence extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evidenceId;

    @Column(nullable = false)
    private Long withdrawalId;

    @Column(nullable = false)
    private Long applicantId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String url;

    @Builder
<<<<<<<HEAD

    public Evidence(Long withdrawalId, Long applicantId, Long postId, String url) {
        this.withdrawalId = withdrawalId;
        this.applicantId = applicantId;
        this.postId = postId;
        this.url = url;
=======
    public Evidence( final Withdrawal withdrawal, final Member member, final Post post){
            this.withdrawal = withdrawal;
            this.member = member;
            this.post = post;
>>>>>>>feat
        }

        @Override
        public boolean equals ( final Object o){
            if (this == o) return true;
            if (!(o instanceof final Evidence evidence)) return false;
            return Objects.equals(evidenceId, evidence.evidenceId);
        }

        @Override
        public int hashCode () {
            return Objects.hash(evidenceId);
        }
    }
