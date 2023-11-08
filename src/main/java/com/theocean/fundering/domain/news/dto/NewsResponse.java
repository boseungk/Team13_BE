package com.theocean.fundering.domain.news.dto;

import com.theocean.fundering.domain.news.domain.News;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class NewsResponse {
    @Getter
    public static class findAllDTO {
        private final List<NewsResponse.newsDTO> updates;
        private final Long cursor;
        private final boolean isLastPage;

        public findAllDTO(final List<NewsResponse.newsDTO> updates, final Long cursor, final boolean isLastPage) {
            this.updates = updates;
            this.cursor = cursor;
            this.isLastPage = isLastPage;
        }

        public boolean getIsLastPage() {
            return isLastPage;
        }
    }

    // findAllDTO의 내부에 들어갈 업데이트 글 정보 DTO
    @Getter
    public static class newsDTO {
        private final Long updateId;
        private final String title;
        private final String content;
        private final LocalDateTime createdAt;

        public newsDTO(final News news) {
            updateId = news.getNewsId();
            title = news.getTitle();
            content = news.getContent();
            createdAt = news.getCreatedAt();
        }
    }
}
