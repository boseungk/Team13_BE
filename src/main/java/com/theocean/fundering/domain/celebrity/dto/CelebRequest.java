package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import lombok.*;

public class CelebRequest {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SaveDTO{
        private String celebName;
        private CelebGender celebGender;
        private CelebCategory celebCategory;
        private String celebGroup;
        @Builder
        public Celebrity mapToEntity() {
            return Celebrity.builder()
                    .celebName(celebName)
                    .celebGender(celebGender)
                    .celebCategory(celebCategory)
                    .celebGroup(celebGroup)
                    .build();
        }
    }
}
