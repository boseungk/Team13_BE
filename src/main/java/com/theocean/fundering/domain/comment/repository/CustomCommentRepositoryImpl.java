package com.theocean.fundering.domain.comment.repository;

import static com.theocean.fundering.domain.comment.domain.QComment.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.comment.domain.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Comment> getCommentList(Long postId, String cursor, int pageSize) {
    return queryFactory
        .selectFrom(comment)
        .where(comment.postId.eq(postId).and(comment.commentOrder.gt(cursor)).and(comment.commentOrder.notLike("%.%")))
        .orderBy(comment.commentId.asc())
        .limit(pageSize)
        .fetch();
  }

  @Override
  public List<Comment> getSubCommentList(Long postId, String parentCommentOrder, String cursor, int pageSize) {
    return queryFactory
            .selectFrom(comment)
            .where(comment.postId.eq(postId).and(comment.commentOrder.gt(cursor)).and(comment.commentOrder.like(parentCommentOrder + "%.%")))
            .orderBy(comment.commentId.asc())
            .limit(pageSize)
            .fetch();
  }
}
