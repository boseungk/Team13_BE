package com.theocean.fundering.domain.celebrity.domain;

import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.global.utils.ApprovalStatus;
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
        indexes = {
                @Index(columnList = "name"), @Index(columnList = "follower_count")
        }
)
@Entity
public class Celebrity extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "celebrity_id")
    private Long celebId;

    @Column(nullable = false, length = 15, name = "name")
    private String celebName;

    @Column(name = "follower_count")
    private int followerCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "gender")
    private CelebGender celebGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "celeb_type")
    private CelebCategory celebCategory;

    @Column(length = 50, name = "celeb_group")
    private String celebGroup;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "approval_status")
    private ApprovalStatus status = ApprovalStatus.PENDING;

    public Celebrity approvalCelebrity() {
        status = ApprovalStatus.APPROVED;
        return this;
    }

    public void addFollowerCount() {
        followerCount += 1;
    }

    public void minusFollowerCount() {
        followerCount -= 1;
    }

    public void updateProfileImage(String thumbnail) {
        profileImage = thumbnail;
    }

    @Builder
    public Celebrity(String celebName, CelebGender celebGender, CelebCategory celebCategory,
                     String celebGroup, String profileImage) {
        this.celebName = celebName;
        this.celebGender = celebGender;
        this.celebCategory = celebCategory;
        this.celebGroup = celebGroup;
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Celebrity celebrity)) return false;
        return Objects.equals(celebId, celebrity.celebId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(celebId);
    }

}