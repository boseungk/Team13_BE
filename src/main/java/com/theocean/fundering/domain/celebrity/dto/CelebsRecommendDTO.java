package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import lombok.Getter;

@Getter
public class CelebsRecommendDTO {

    private final Long celebId;
    private final String celebName;
    private final String celebProfile;
    private final int followingCount;
    private final boolean isFollowing;

    private CelebsRecommendDTO(final Celebrity celebrity, final int followerCount, final boolean following) {
        celebId = celebrity.getCelebId();
        celebName = celebrity.getCelebName();
        celebProfile = celebrity.getProfileImage();
        followingCount = followerCount;
        isFollowing = following;
    }

    public static CelebsRecommendDTO of(final Celebrity celebrity, final int followerCount, final boolean following) {
        return new CelebsRecommendDTO(celebrity, followerCount, following);
    }
}
