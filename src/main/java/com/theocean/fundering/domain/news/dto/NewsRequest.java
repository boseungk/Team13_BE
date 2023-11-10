package com.theocean.fundering.domain.news.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NewsRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SaveDTO {
        private String title;
        private String content; // 마크다운 형식의 문자열이 저장됨
    }
}
