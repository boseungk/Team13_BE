package com.theocean.fundering.domain.member.dto;

import lombok.Getter;

public class AdminResponse {

    @Getter
    public static class FindAllDTO {
        private final Long adminId;
        private final String profile;
        private final String nickname;

        public FindAllDTO(final Long adminId, final String profile, final String nickname) {
            this.adminId = adminId;
            this.profile = profile;
            this.nickname = nickname;
        }
        public static AdminResponse.FindAllDTO fromEntity(final Long adminId, final String profile, final String nickname) {
            return new AdminResponse.FindAllDTO(adminId, profile, nickname);
        }
    }
}
