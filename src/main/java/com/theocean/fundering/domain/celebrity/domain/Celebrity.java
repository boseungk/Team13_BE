package com.theocean.fundering.domain.celebrity.domain;

import com.theocean.fundering.domain.celebrity.domain.constant.ApprovalStatus;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "celebrity",
        indexes = @Index(columnList = "celebName")
)
@Entity
public class Celebrity extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long celebId;

    @Column(nullable = false, length = 15)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebGender celebGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebType celebType;

    @Column(length = 50)
    private String celebGroup;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Builder
    public Celebrity(final String celebName, final CelebGender celebGender, final CelebType celebType,
                     final String celebGroup, final String profileImage) {
        this.celebName = celebName;
        this.celebGender = celebGender;
        this.celebType = celebType;
        this.celebGroup = celebGroup;
        this.profileImage = profileImage;
    }

    public Celebrity approvalCelebrity() {
        status = ApprovalStatus.APPROVED;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Celebrity celebrity)) return false;
        return Objects.equals(celebId, celebrity.celebId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(celebId);
    }
}