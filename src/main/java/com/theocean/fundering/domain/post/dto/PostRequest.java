package com.theocean.fundering.domain.post.dto;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

public class PostRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PostWriteDTO { // 게시글 작성 DTO
        private Long celebId;
        private String title;
        private String introduction;
        private int targetPrice;
        private String deadline;
        private LocalDateTime createdAt;

        @Builder
        public PostWriteDTO(final Long celebId, final String title, final String introduction, final int targetPrice, final String deadline) {
            this.celebId = celebId;
            this.title = title;
            this.introduction = introduction;
            this.targetPrice = targetPrice;
            this.deadline = deadline;
            createdAt = LocalDateTime.now();
        }

        public Post toEntity(final Member writer, final Celebrity celebrity, final String thumbnail, final LocalDateTime deadline, final PostStatus postStatus) {
            return Post.builder()
                    .writer(writer)
                    .celebrity(celebrity)
                    .title(title)
                    .introduction(introduction)
                    .thumbnail(thumbnail)
                    .targetPrice(targetPrice)
                    .deadline(deadline)
                    .postStatus(postStatus)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PostEditDTO {
        private String title;
        private String introduction;
        private int targetPrice;
        private String deadline;
        private LocalDateTime modifiedAt;

        @Builder
        public PostEditDTO(final String title, final String introduction, final int targetPrice, final String deadline) {
            this.title = title;
            this.introduction = introduction;
            this.targetPrice = targetPrice;
            this.deadline = deadline;
            modifiedAt = LocalDateTime.now();
        }

        public Post toEntity(final LocalDateTime deadline) {
            return Post.builder()
                    .title(title)
                    .introduction(introduction)
                    .targetPrice(targetPrice)
                    .deadline(deadline)
                    .build();
        }
    }

}
