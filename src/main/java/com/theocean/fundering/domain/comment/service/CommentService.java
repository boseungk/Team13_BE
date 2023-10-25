package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.comment.repository.CustomCommentRepositoryImpl;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception404;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CustomCommentRepositoryImpl customCommentRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommentValidator commentValidator;

    /**
     * (기능) 댓글 작성
     */
    @Transactional
    public void createComment(
            final Long memberId, final Long postId, final CommentRequest.saveDTO request) {

        commentValidator.validateMemberAndPost(memberId, postId);

        final Comment newComment = buildBaseComment(memberId, postId, request.getContent());

        createParentComment(postId, newComment);
    }

    // 기본 댓글 객체 생성
    private Comment buildBaseComment(final Long memberId, final Long postId, final String content) {
        return Comment.builder().writerId(memberId).postId(postId).content(content).build();
    }

    // 원댓글 생성
    private void createParentComment(final Long postId, final Comment newComment) {
        final String maxCommentOrder = commentRepository.findMaxCommentOrder(postId);
        final int newCommentOrder = calculateCommentOrder(maxCommentOrder);

        newComment.updateCommentOrder(String.valueOf(newCommentOrder));
        commentRepository.save(newComment);
    }

    // 생성 댓글의 commentOrder 계산
    private int calculateCommentOrder(final String maxCommentOrder) {
        if (null != maxCommentOrder) {
            final String[] parts = maxCommentOrder.split("\\.");
            return Integer.parseInt(parts[0]) + 1;
        }
        return 1;
    }

    /**
     * (기능) 대댓글 작성
     */
    @Transactional
    public void createSubComment(
            final Long memberId,
            final Long postId,
            final Long parentCommentId,
            final CommentRequest.saveDTO request) {

        commentValidator.validateMemberAndPost(memberId, postId);

        commentValidator.validateCommentExistence(parentCommentId);

        final String content = request.getContent();

        final Comment newComment = buildBaseComment(memberId, postId, content);

        final String parentCommentOrder = findCommentOrder(parentCommentId);

        commentValidator.validateDepthLimit(parentCommentOrder);

        createChildComment(postId, parentCommentOrder, newComment);
    }

    // 원댓글의 commentOrder 반환
    private String findCommentOrder(final Long commentId) {
        final Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        return comment.getCommentOrder();
    }

    // 대댓글 생성
    private void createChildComment(
            final Long postId, final String parentCommentOrder, final Comment newComment) {

        final int replyCount = commentValidator.validateReplyLimit(postId, parentCommentOrder);

        final String newCommentOrder = parentCommentOrder + "." + (replyCount + 1);

        newComment.updateCommentOrder(newCommentOrder);
        commentRepository.save(newComment);
    }

    /**
     * (기능) 댓글 목록 조회
     */
    public CommentResponse.findAllDTO getComments(final long postId, final Pageable pageable) {

        commentValidator.validatePostExistence(postId);

        final Page<Comment> commentPage = customCommentRepository.getCommentsPage(postId, pageable);
        final List<Comment> comments = commentPage.getContent();

        final List<CommentResponse.commentDTO> commentsDTOs = convertToCommentDTOs(comments);

        final boolean isLastPage = commentPage.isLast();

        final int currentPage = pageable.getPageNumber();

        return new CommentResponse.findAllDTO(commentsDTOs, currentPage, isLastPage);
    }

    // 댓글 DTO 변환
    private List<CommentResponse.commentDTO> convertToCommentDTOs(final List<Comment> comments) {

        return comments.stream().map(this::createCommentDTO).collect(Collectors.toList());
    }

    private CommentResponse.commentDTO createCommentDTO(final Comment comment) {
        final Member writer =
                memberRepository
                        .findById(comment.getWriterId())
                        .orElseThrow(() -> new Exception404("존재하지 않는 회원입니다: " + comment.getWriterId()));

        return CommentResponse.commentDTO.fromEntity(
                comment, writer.getNickname(), writer.getProfileImage());
    }

    /**
     * (기능) 대댓글 목록 조회
     */
    public CommentResponse.findAllDTO getSubComments(
            final long postId, final long parentCommentId, final Pageable pageable) {

        commentValidator.validatePostExistence(postId);

        commentValidator.validateCommentExistence(parentCommentId);

        final Page<Comment> commentPage =
                customCommentRepository.getSubCommentsPage(
                        postId, findCommentOrder(parentCommentId), pageable);
        final List<Comment> comments = commentPage.getContent();

        final List<CommentResponse.commentDTO> commentsDTOs = convertToCommentDTOs(comments);

        final boolean isLastPage = commentPage.isLast();
        final int currentPage = pageable.getPageNumber();

        return new CommentResponse.findAllDTO(commentsDTOs, currentPage, isLastPage);
    }

    /**
     * (기능) 댓글 삭제
     */
    @Transactional
    public void deleteComment(final Long memberId, final Long postId, final Long commentId) {
        final Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        commentValidator.validatePostExistence(postId);
        commentValidator.validateCommentOwner(memberId, comment);

        commentRepository.delete(comment);
    }
}
