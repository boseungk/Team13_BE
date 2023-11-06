package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import lombok.Getter;

@Getter
public class CelebsRecommendResponseDTO {

    private final Long celebId;
    private final String celebName;
    private final String celebProfileImage;
    private final int followingCount;
    private final boolean isFollowing;

    private CelebsRecommendResponseDTO(final Celebrity celebrity, int followerCount, boolean following) {
        celebId = celebrity.getCelebId();
        celebName = celebrity.getCelebName();
        celebProfileImage = celebrity.getProfileImage();
        followingCount = followerCount;
        isFollowing = following;
    }
    public static CelebsRecommendResponseDTO of(Celebrity celebrity, int followerCount, boolean following) {
        return new CelebsRecommendResponseDTO(celebrity, followerCount, following);
    }
}
