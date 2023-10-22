package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.comment.repository.CustomCommentRepositoryImpl;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import com.theocean.fundering.global.errors.exception.Exception500;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

  private final CustomCommentRepositoryImpl customCommentRepository;
  private final CommentRepository commentRepository;
  private final MemberRepository memberRepository;
  private final PostRepository postRepository;
  private static final int REPLY_LIMIT = 30;

  /** (기능) 댓글 작성 */
  @Transactional
  public void createComment(
      final Long memberId, final Long postId, final CommentRequest.saveDTO request) {

    validateMemberAndPost(memberId, postId);

    final Comment newComment = buildBaseComment(memberId, postId, request.getContent());

    createParentComment(postId, newComment);
  }

  // 작성자와 게시글 존재 유무 체크
  private void validateMemberAndPost(final Long memberId, final Long postId) {
    if (!memberRepository.existsById(memberId))
      throw new Exception404("존재하지 않는 회원입니다: " + memberId);

    validatePostExistence(postId);
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

  /** (기능) 대댓글 작성 */
  @Transactional
  public void createSubComment(
      final Long memberId,
      final Long postId,
      final Long commentId,
      final CommentRequest.saveDTO request) {

    validateMemberAndPost(memberId, postId);

    validateCommentExistence(commentId);

    final String content = request.getContent();

    final Comment newComment = buildBaseComment(memberId, postId, content);

    final String parentCommentOrder = findParentCommentOrder(commentId);

    createChildComment(postId, parentCommentOrder, newComment);
  }

  // 원댓글의 commentOrder 반환
  private String findParentCommentOrder(final Long commentId) {
    final Comment parentComment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

    return parentComment.getCommentOrder();
  }

  // 대댓글 생성
  private void createChildComment(
      final Long postId, final String parentCommentOrder, final Comment newComment) {

    if (parentCommentOrder.contains(".")) {
      throw new Exception400("대댓글에는 댓글을 달 수 없습니다.");
    }

    final Comment parentComment =
        commentRepository
            .getComment(postId, parentCommentOrder)
            .orElseThrow(() -> new Exception400("원댓글을 찾을 수 없습니다."));

    final int replyCount = commentRepository.countReplies(postId, parentCommentOrder + "%.%");
    if (REPLY_LIMIT <= replyCount) throw new Exception400("더 이상 대댓글을 달 수 없습니다.");

    final String newCommentOrder = parentCommentOrder + "." + (replyCount + 1);

    newComment.updateCommentOrder(newCommentOrder);
    commentRepository.save(newComment);
  }

  /** (기능) 댓글 목록 조회 */
  public CommentResponse.findAllDTO getComments(
      final long postId, final Long cursor, final int pageSize) {

    validatePostExistence(postId);

    List<Comment> comments;
    try {
      comments = customCommentRepository.getCommentList(postId, cursor, pageSize + 1);
    } catch (RuntimeException e) {
      throw new Exception500("댓글 조회 도중 문제가 발생했습니다.");
    }

    final boolean isLastPage = comments.size() <= pageSize;

    if (!isLastPage) comments = comments.subList(0, pageSize);

    final var commentsDTOs = convertToCommentDTOs(comments);

    Long lastCursor = null;

    if (!comments.isEmpty()) {
      final Comment lastComment = comments.get(comments.size() - 1);
      lastCursor = lastComment.getCommentId();
    }

    return new CommentResponse.findAllDTO(commentsDTOs, lastCursor, isLastPage);
  }

  // 게시글 존재 유무 체크
  private void validatePostExistence(final long postId) {
    if (!postRepository.existsById(postId)) {
      throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);
    }
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

  /** (기능) 대댓글 목록 조회 */
  public CommentResponse.findAllDTO getSubComments(
      final long postId, final long commentId, final long cursor, final int pageSize) {

    validatePostExistence(postId);

    validateCommentExistence(commentId);

    final String parentCommentOrder = findParentCommentOrder(commentId);

    List<Comment> comments;
    try {
      comments =
          customCommentRepository.getSubCommentList(
              postId, parentCommentOrder, cursor, pageSize + 1);
    } catch (RuntimeException e) {
      throw new Exception500("댓글 조회 도중 문제가 발생했습니다.");
    }

    final boolean isLastPage = comments.size() <= pageSize;

    if (!isLastPage) comments = comments.subList(0, pageSize);

    final var commentsDTOs = convertToCommentDTOs(comments);

    Long lastCursor = null;

    if (!comments.isEmpty()) {
      final Comment lastComment = comments.get(comments.size() - 1);
      lastCursor = lastComment.getCommentId();
    }

    return new CommentResponse.findAllDTO(commentsDTOs, lastCursor, isLastPage);
  }

  // 대댓글 삭제 여부, 존재 여부 판별
  private void validateCommentExistence(final long commentId) {
    final Comment parentComment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

    if (parentComment.isDeleted()) throw new Exception400("삭제된 댓글입니다.");
  }

  /** (기능) 댓글 삭제 */
  @Transactional
  public void deleteComment(final Long memberId, final Long postId, final Long commentId) {
    final Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

    if (!postRepository.existsById(postId)) throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);

    if (!memberId.equals(comment.getWriterId())) throw new Exception403("댓글 삭제 권한이 없습니다.");

    commentRepository.delete(comment);
  }
}
