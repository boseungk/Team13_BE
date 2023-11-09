package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MemberResponse {

    @Getter
    @RequiredArgsConstructor
    public static class SettingDTO {
        private final String nickname;
        private final String phoneNumber;
        private final String profileImage;

        private SettingDTO(final Member member) {
            nickname = member.getNickname();
            phoneNumber = member.getPhoneNumber();
            profileImage = member.getProfileImage();
        }

        public static SettingDTO from(final Member member) {
            return new SettingDTO(member);
        }
    }

    @Getter
    public static class FollowingCelebsDTO {
        private final Long celebId;
        private final String profileImage;
        private final String celebName;
        private final int followerCount;

        private FollowingCelebsDTO(final Celebrity celebrity, final int follower) {
            celebId = celebrity.getCelebId();
            profileImage = celebrity.getProfileImage();
            celebName = celebrity.getCelebName();
            followerCount = follower;
        }

        public static FollowingCelebsDTO of(final Celebrity celebrity, final int followerCount) {
            return new FollowingCelebsDTO(celebrity, followerCount);
        }
    }
}
