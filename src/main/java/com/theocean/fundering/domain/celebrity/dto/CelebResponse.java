package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class CelebResponse {
    @Getter
    public static class DetailsDTO {

        private final String celebName;
        private final CelebGender celebGender;
        private final CelebCategory celebCategory;
        private final String celebGroup;
        private final String profileImage;
        private final int followerCount;
        private final int followerRank;

        private DetailsDTO(final Celebrity celebrity, final int followerCount, final int followerRank) {
            celebName = celebrity.getCelebName();
            celebGender = celebrity.getCelebGender();
            celebCategory = celebrity.getCelebCategory();
            celebGroup = celebrity.getCelebGroup();
            profileImage = celebrity.getProfileImage();
            this.followerCount = followerCount;
            this.followerRank = followerRank;
        }

        public static DetailsDTO of(final Celebrity celebrity, final int followerCount, final int followerRank, final List<Post> postsByCelebId) {
            return new DetailsDTO(celebrity, followerCount, followerRank);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class FundingDTO {

        private final Long postId;
        private final Long userId;
        private final String nickname;
        private final Long celebId;
        private final String celebName;
        private final String title;
        private final String content;
        private final int participants;
        private final int targetPrice;
    }

    @Getter
    @AllArgsConstructor
    public static class ListDTO {

        private Long celebId;
        private String celebName;
        private CelebGender celebGender;
        private CelebCategory celebCategory;
        private String celeGroup;
        private String profileImage;
        private int followerCount;
        private int postId = 1;
        private PostStatus postStatus = PostStatus.ONGOING;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FundingListDTO {

        private final Long celebId;
        private final String celebName;
        private final CelebGender celebGender;
        private final CelebCategory celebCategory;
        private final String celeGroup;
        private final String profileImage;
        private final int fundingAmount;
        private final int ongoingFundingCount;
        private final boolean isFollowing;
        private final int celebRank;

        private FundingListDTO(CelebResponse.ListDTO fundingDTO, int totalFunding, int ongoingCount, int celebRanking, boolean Following) {
            celebId = fundingDTO.celebId;
            celebName = fundingDTO.celebName;
            celebGender = fundingDTO.celebGender;
            celebCategory = fundingDTO.celebCategory;
            celeGroup = fundingDTO.celeGroup;
            profileImage = fundingDTO.profileImage;
            fundingAmount = totalFunding;
            ongoingFundingCount = ongoingCount;
            celebRank = celebRanking;
            isFollowing = Following;
        }

        public static FundingListDTO of(CelebResponse.ListDTO fundingDTO, int totalFunding, int ongoingCount, int celebRanking, boolean Following) {
            return new FundingListDTO(fundingDTO, totalFunding, ongoingCount, celebRanking, Following);
        }
    }

    @Getter
    public static class ProfileDTO {

        private final Long celebId;
        private final String celebName;
        private final String celebProfileImage;
        private final int followingCount;
        private final boolean isFollowing;

        private ProfileDTO(final Celebrity celebrity, final int followerCount, final boolean following) {
            celebId = celebrity.getCelebId();
            celebName = celebrity.getCelebName();
            celebProfileImage = celebrity.getProfileImage();
            followingCount = followerCount;
            isFollowing = following;
        }

        public static ProfileDTO of(final Celebrity celebrity, final int followerCount, final boolean following) {
            return new ProfileDTO(celebrity, followerCount, following);
        }
    }

}
