package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.post.domain.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class CelebDetailsResponseDTO {

    private final String celebName;
    private final CelebGender celebGender;
    private final CelebCategory celebCategory;
    private final String celebGroup;
    private final String profileImage;
    private final int followerCount;
    private final int followerRank;

    private CelebDetailsResponseDTO(final Celebrity celebrity, int followerCount, int followerRank) {
        celebName = celebrity.getCelebName();
        celebGender = celebrity.getCelebGender();
        celebCategory = celebrity.getCelebCategory();
        celebGroup = celebrity.getCelebGroup();
        profileImage = celebrity.getProfileImage();
        this.followerCount = followerCount;
        this.followerRank = followerRank;
    }
    public static CelebDetailsResponseDTO of(final Celebrity celebrity, int followerCount, int followerRank, List<Post> postsByCelebId){
        return new CelebDetailsResponseDTO(celebrity, followerCount, followerRank);
    }
}
