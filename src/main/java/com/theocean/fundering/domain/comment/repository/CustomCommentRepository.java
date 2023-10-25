package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;
import java.util.List;

public interface CustomCommentRepository {
  List<Comment> getCommentList(Long postId, Long cursor, int pageSize);

  List<Comment> getSubCommentList(
      Long postId, String parentCommentOrder, Long cursor, int pageSize);
}
