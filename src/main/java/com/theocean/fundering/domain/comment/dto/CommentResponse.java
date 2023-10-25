package com.theocean.fundering.domain.comment.dto;

import com.theocean.fundering.domain.comment.domain.Comment;

import java.util.List;

import lombok.Getter;

public class CommentResponse {

    // 전체 댓글 조회 DTO
    @Getter
    public static class findAllDTO {
        private final List<commentDTO> comments;
        private final int currentPage;
        private final boolean isLastPage;

        public findAllDTO(
                final List<commentDTO> comments, final int currentPage, final boolean isLastPage) {
            this.comments = comments;
            this.currentPage = currentPage;
            this.isLastPage = isLastPage;
        }

        public boolean getIsLastPage() {
            return isLastPage;
        }
    }

    // findAllDTO의 내부에 들어갈 댓글 정보 DTO
    @Getter
    public static class commentDTO {
        private final Long commentId;
        private final Long writerId;
        private final String writerName;
        private final String writerProfile;
        private final String content;
        private final int depth;
        private final long createdAt;

        commentDTO(final Comment comment, final String writerName, final String writerProfile) {
            commentId = comment.getCommentId();
            writerId = comment.getWriterId();
            this.writerName = writerName;
            this.writerProfile = writerProfile;
            content = comment.getContent();
            depth = comment.getDepth();
            createdAt = comment.getEpochSecond();
        }

        public static commentDTO fromEntity(
                final Comment comment, final String nickname, final String profileImage) {
            return new commentDTO(comment, nickname, profileImage);
        }
    }
}
