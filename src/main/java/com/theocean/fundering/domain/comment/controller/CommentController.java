package com.theocean.fundering.domain.comment.controller;

import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.service.CreateCommentService;
import com.theocean.fundering.domain.comment.service.DeleteCommentService;
import com.theocean.fundering.domain.comment.service.ReadCommentService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CreateCommentService createCommentService;
    private final ReadCommentService readCommentService;
    private final DeleteCommentService deleteCommentService;

    // (기능) 댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> createComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final CommentRequest.SaveDTO commentRequest,
            @PathVariable final long postId) {

        final Long memberId = 1L; // Long memberId = userDetails.getMember().getUserId();
        createCommentService.createComment(memberId, postId, commentRequest);

        return ApiResult.success(null);
    }

    // (기능) 대댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> createSubComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final CommentRequest.SaveDTO commentRequest,
            @PathVariable final long postId,
            @PathVariable final long commentId) {

        final Long memberId = 1L; // Long memberId = userDetails.getMember().getUserId();
        createCommentService.createSubComment(memberId, postId, commentId, commentRequest);

        return ApiResult.success(null);
    }

    // (기능) 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> readComments(
            @PathVariable final long postId, @PageableDefault(size = 10) final Pageable pageable) {

        final CommentResponse.FindAllDTO response = readCommentService.getComments(postId, pageable);

        return ApiResult.success(response);
    }

    // (기능) 대댓글 목록 조회
    @GetMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> readSubComments(
            @PathVariable final long postId,
            @PathVariable final long commentId,
            @PageableDefault(size = 10) final Pageable pageable) {

        final CommentResponse.FindAllDTO response =
                readCommentService.getSubComments(postId, commentId, pageable);

        return ApiResult.success(response);
    }

    // (기능) 댓글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> deleteComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final long postId,
            @PathVariable final long commentId) {

        final Long memberId = 1L; // userDetails.getMember().getUserId();
        deleteCommentService.deleteComment(memberId, postId, commentId);

        return ApiResult.success(null);
    }
}
