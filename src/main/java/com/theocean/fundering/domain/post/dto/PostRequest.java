package com.theocean.fundering.domain.post.dto;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PostRequest {
    private static long toEpochSecond(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    private static LocalDateTime toLocalDateTime(final long epochSecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PostWriteDTO { // 게시글 작성 DTO
        private Long celebId;
        private String title;
        private String introduction;
        private String thumbnailURL;
        private int targetPrice;
        private Long deadline;
        private Long createdAt;

        public Post toEntity(Member writer, Celebrity celebrity) {
            return Post.builder()
                    .writer(writer)
                    .celebrity(celebrity)
                    .title(title)
                    .introduction(introduction)
                    .thumbnail(thumbnailURL)
                    .targetPrice(targetPrice)
                    .deadline(toLocalDateTime(deadline))
                    .build();
        }

        @Builder
        public PostWriteDTO(Long celebId, String title, String introduction, String thumbnail, int targetPrice, Long deadline) {
            this.celebId = celebId;
            this.title = title;
            this.introduction = introduction;
            this.thumbnailURL = thumbnail;
            this.targetPrice = targetPrice;
            this.deadline = deadline;
            this.createdAt = toEpochSecond(LocalDateTime.now());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PostEditDTO {
        private String title;
        private String introduction;
        private String thumbnail;
        private int targetPrice;
        private Long deadline;
        private Long modifiedAt;

        public Post toEntity() {
            return Post.builder()
                    .title(title)
                    .introduction(introduction)
                    .thumbnail(thumbnail)
                    .targetPrice(targetPrice)
                    .deadline(toLocalDateTime(deadline))
                    .build();
        }

        @Builder
        public PostEditDTO(String title, String introduction, String thumbnail, int targetPrice, Long deadline) {
            this.title = title;
            this.introduction = introduction;
            this.thumbnail = thumbnail;
            this.targetPrice = targetPrice;
            this.deadline = deadline;
            this.modifiedAt = toEpochSecond(LocalDateTime.now());
        }
    }
}
