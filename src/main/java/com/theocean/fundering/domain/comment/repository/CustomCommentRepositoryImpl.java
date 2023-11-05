package com.theocean.fundering.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.theocean.fundering.domain.comment.domain.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> getCommentsPage(final Long postId, final Pageable pageable) {
        final List<Comment> content =
                queryFactory
                        .selectFrom(comment)
                        .where(
                                comment.postId.eq(postId),
                                comment.commentOrder.notLike("%.%"),
                                comment.isDeleted.eq(false))
                        .orderBy(comment.commentId.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        final JPAQuery<Long> countQuery =
                queryFactory
                        .select(comment.count())
                        .from(comment)
                        .where(
                                comment.postId.eq(postId),
                                comment.commentOrder.notLike("%.%"),
                                comment.isDeleted.eq(false));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Comment> getSubCommentsPage(
            final Long postId, final String parentCommentOrder, final Pageable pageable) {
        final List<Comment> content =
                queryFactory
                        .selectFrom(comment)
                        .where(
                                comment.postId.eq(postId),
                                comment.commentOrder.like(parentCommentOrder + ".%"),
                                comment.isDeleted.eq(false))
                        .orderBy(comment.commentId.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        final JPAQuery<Long> countQuery =
                queryFactory
                        .select(comment.count())
                        .from(comment)
                        .where(
                                comment.postId.eq(postId),
                                comment.commentOrder.like(parentCommentOrder + ".%"),
                                comment.isDeleted.eq(false));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
