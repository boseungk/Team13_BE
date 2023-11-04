package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CelebRequestDTO {
    private final String celebName;
    private final CelebGender celebGender;
    private final CelebType celebType;
    private final String celebGroup;
    private final String profileImage;

    public Celebrity mapToEntity(){
        return Celebrity.builder()
                .celebName(celebName)
                .celebGender(celebGender)
                .celebType(celebType)
                .celebGroup(celebGroup)
                .profileImage(profileImage)
                .build();
    }
}
