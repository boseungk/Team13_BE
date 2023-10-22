package com.theocean.fundering.domain.comment.dto;

import com.theocean.fundering.domain.comment.domain.Comment;
import java.util.List;
import lombok.Getter;

public class CommentResponse {

  // 전체 댓글 조회 DTO
  @Getter
  public static class findAllDTO {
    private final List<commentDTO> comments;
    private final Long lastCursor;
    private final boolean isLastPage;

    public findAllDTO(List<commentDTO> comments, Long lastCursor, boolean isLastPage) {
      this.comments = comments;
      this.lastCursor = lastCursor;
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
    private final boolean isDeleted;
    private final long createdAt;

    public commentDTO(Comment comment, String writerName, String writerProfile) {
      this.commentId = comment.getCommentId();
      this.writerId = comment.getWriterId();
      this.writerName = writerName;
      this.writerProfile = writerProfile;
      this.content = comment.getContent();
      this.depth = comment.getDepth();
      this.isDeleted = comment.isDeleted();
      this.createdAt = comment.getEpochSecond();
    }

    public boolean getIsDeleted() {
      return isDeleted;
    }

    public static commentDTO fromEntity(Comment comment, String nickname, String profileImage) {
      return new commentDTO(comment, nickname, profileImage);
    }
  }
}
