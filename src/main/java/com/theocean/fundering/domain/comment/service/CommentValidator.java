package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentValidator {

    private static final int REPLY_LIMIT = 30;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    void validateMemberAndPost(final Long memberId, final Long postId) {
        if (!memberRepository.existsById(memberId))
            throw new Exception404(ErrorCode.ER01);

        validatePostExistence(postId);
    }

    void validatePostExistence(final long postId) {
        if (!postRepository.existsById(postId)) throw new Exception404(ErrorCode.ER03);
    }

    void validateCommentExistence(final long commentId) {
        final Comment parentComment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404(ErrorCode.ER09));

        if (parentComment.isDeleted()) throw new Exception400(ErrorCode.ER10);
    }

    void validateCommentOwner(final Long memberId, final Comment comment) {

        if (!memberId.equals(comment.getWriterId())) throw new Exception403(ErrorCode.ER11);
    }

    void validateDepthLimit(final String commentOrder) {
        if (commentOrder.contains(".")) throw new Exception400(ErrorCode.ER12);
    }

    int validateReplyLimit(final Long postId, final String parentCommentOrder) {
        final int replyCount = commentRepository.countReplies(postId, parentCommentOrder + "%.%");
        if (REPLY_LIMIT <= replyCount) throw new Exception400(ErrorCode.ER13);
        return replyCount;
    }

    @Cacheable(key = "#postId + '_' + #commentOrder", value = "replyCounts")
    public int getReplyCount(final Long postId, final String commentOrder) {
        if (commentOrder.contains(".")) return 0; // 대댓글의 경우, 0을 반환

        return commentRepository.countReplies(postId, commentOrder + "%.%");
    }

    // 원댓글의 commentOrder 반환
    String findCommentOrder(final Long commentId) {
        final Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404(ErrorCode.ER09));

        return comment.getCommentOrder();
    }
}
