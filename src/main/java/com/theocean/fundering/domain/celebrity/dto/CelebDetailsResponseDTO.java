package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import lombok.Getter;

@Getter
public class CelebDetailsResponseDTO {

    private final String celebName;
    private final CelebGender celebGender;
    private final CelebType celebType;
    private final String celebGroup;
    private final String profileImage;

    private CelebDetailsResponseDTO(Celebrity celebrity) {
        this.celebName = celebrity.getCelebName();
        this.celebGender = celebrity.getCelebGender();
        this.celebType = celebrity.getCelebType();
        this.celebGroup = celebrity.getCelebGroup();
        this.profileImage = celebrity.getProfileImage();
    }
    public static CelebDetailsResponseDTO from(Celebrity celebrity){
        return new CelebDetailsResponseDTO(celebrity);
    }
}
