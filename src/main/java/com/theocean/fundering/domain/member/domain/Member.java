package com.theocean.fundering.domain.member.domain;

import com.theocean.fundering.domain.member.domain.constant.UserRole;
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
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member",
        indexes = @Index(columnList = "email", unique = true)
)
@Entity
public class Member extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long userId;

    @Setter
    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 11)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @Column(name = "profile_image")
    private String profileImage; // 프로필 이미지

    public void changeNickname(final String nickname){
        this.nickname = nickname;
    }

    public void setPassword(final String password){
        this.password = password;
    }

    public void updateRefreshToken(final String updateRefreshToken) {
        refreshToken = updateRefreshToken;
    }

    public void changePhoneNumber(final String phoneNumber){
        this.phoneNumber = phoneNumber;
    }


    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Member(final Long userId, final String nickname, final String password, final String email, final UserRole userRole, final String profileImage) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Member member)) return false;
        return Objects.equals(userId, member.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }


    public void updateUserSetting(String nickname, String encodePassword, String phoneNumber, String profileImage) {
        changeNickname(nickname);
        setPassword(encodePassword);
        changePhoneNumber(phoneNumber);
        changeProfileImage(profileImage);
    }
}