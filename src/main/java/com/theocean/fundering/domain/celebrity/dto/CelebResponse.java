package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class CelebResponse {
    @Getter
    public static class DetailsDTO {

        private final String celebName;
        private final CelebGender celebGender;
        private final CelebCategory celebCategory;
        private final String celebGroup;
        private final String profileImage;
        private final int fundingAmount;
        private final int ongoingCount;
        private final int followerCount;
        private final int followerRank;
        private final boolean isFollow;

        private DetailsDTO(final Celebrity celebrity, final int fundingAmount, final int ongoingCount, final int followerRank, final boolean isFollow) {
            celebName = celebrity.getCelebName();
            celebGender = celebrity.getCelebGender();
            celebCategory = celebrity.getCelebCategory();
            celebGroup = celebrity.getCelebGroup();
            profileImage = celebrity.getProfileImage();
            followerCount = celebrity.getFollowerCount();
            this.fundingAmount = fundingAmount;
            this.ongoingCount = ongoingCount;
            this.followerRank = followerRank;
            this.isFollow = isFollow;
        }

        public static DetailsDTO of(final Celebrity celebrity, final int fundingAmount, final int ongoingCount, final int followerRank, final boolean isFollowing) {
            return new DetailsDTO(celebrity, fundingAmount, ongoingCount, followerRank, isFollowing);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class FundingDataDTO {
        private final Long postId;
        private final Long writerId;
        private final String nickname;
        private final String writerImg;
        private final Long celebId;
        private final String celebName;
        private final String celebImg;
        private final String title;
        private final String content;
        private final String postThumbnail;
        private final int targetPrice;
        private final LocalDateTime deadline;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;
        private final int participants;
        private final int heartCount;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FundingDTO {
        private final Long postId;
        private final Long writerId;
        private final String writer;
        private final String writerImg;
        private final Long celebId;
        private final String celebName;
        private final String celebImg;
        private final String title;
        private final String content;
        private final String thumbnail;
        private final int targetPrice;
        private final int currentAmount;
        private final LocalDateTime deadline;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;
        private final int participant;
        private final boolean eqWriter;
        private final int heartCount;
        private final boolean isFollow;
        private final boolean isHeart;

        private FundingDTO(final FundingDataDTO fundingDataDTO, final int balance, final boolean isWriter, final boolean isFollow, final boolean isHeart) {
            postId = fundingDataDTO.getPostId();
            writerId = fundingDataDTO.getWriterId();
            writer = fundingDataDTO.getWriterImg();
            writerImg = fundingDataDTO.getWriterImg();
            celebId = fundingDataDTO.getCelebId();
            celebName = fundingDataDTO.getCelebName();
            celebImg = fundingDataDTO.getCelebImg();
            title = fundingDataDTO.getTitle();
            content = fundingDataDTO.getContent();
            thumbnail = fundingDataDTO.getPostThumbnail();
            targetPrice = fundingDataDTO.getTargetPrice();
            currentAmount = fundingDataDTO.getParticipants();
            deadline = fundingDataDTO.getDeadline();
            createdAt = fundingDataDTO.getCreatedAt();
            modifiedAt = fundingDataDTO.getModifiedAt();
            participant = fundingDataDTO.getParticipants();
            heartCount = fundingDataDTO.getHeartCount();
            eqWriter = isWriter;
            this.isFollow = isFollow;
            this.isHeart = isHeart;
        }

        public static FundingDTO of(final FundingDataDTO fundingDataDTO, final int balance, final boolean isWriter, final boolean isFollow, final boolean isHeart) {
            return new FundingDTO(fundingDataDTO, balance, isWriter, isFollow, isHeart);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ListForApprovalDTO {

        private final Long celebId;
        private final String celebName;
        private final CelebGender celebGender;
        private final CelebCategory celebCategory;
        private final String celeGroup;
        private final String profileImage;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ListDTO {

        private final Long celebId;
        private final String celebName;
        private final CelebGender celebGender;
        private final CelebCategory celebCategory;
        private final String celeGroup;
        private final String profileImage;
        private final int followerCount;
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
        private final int followerCount;

        private FundingListDTO(final ListDTO fundingDTO, final int totalFunding, final int ongoingCount, final Integer celebRanking, final boolean Following, final int followerCount) {
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
            this.followerCount = followerCount;
        }

        public static FundingListDTO of(final ListDTO fundingDTO, final int totalFunding, final int ongoingCount, final Integer celebRanking, final boolean Following, final int followerCount) {
            return new FundingListDTO(fundingDTO, totalFunding, ongoingCount, celebRanking, Following, followerCount);
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
