package com.theocean.fundering.domain.comment.controller;

import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.service.CreateCommentService;
import com.theocean.fundering.domain.comment.service.DeleteCommentService;
import com.theocean.fundering.domain.comment.service.ReadCommentService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "COMMENT", description = "펀딩 소통의 공간 댓글 관련 API")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CreateCommentService createCommentService;
    private final ReadCommentService readCommentService;
    private final DeleteCommentService deleteCommentService;

    // (기능) 댓글 작성
    @Operation(summary = "댓글 작성", description = "해당 postId에 해당하는 게시글에 댓글을 작성한다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> createComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid @Schema(implementation = CommentRequest.SaveDTO.class) final CommentRequest.SaveDTO commentRequest,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId
    ){
        final Long memberId = userDetails.getId();
        createCommentService.createComment(memberId, postId, commentRequest);

        return ApiResult.success(null);
    }

    // (기능) 대댓글 작성
    @Operation(summary = "대댓글 작성", description = "해당 postId와 commentId에 해당하는 댓글에 대댓글을 작성한다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> createSubComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final CommentRequest.SaveDTO commentRequest,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(description = "댓글의 PK") @PathVariable final long commentId
    ){
        final Long memberId = userDetails.getId();
        createCommentService.createSubComment(memberId, postId, commentId, commentRequest);

        return ApiResult.success(null);
    }

    // (기능) 댓글 목록 조회
    @Operation(summary = "댓글 목록 조회", description = "해당 postId에 해당하는 게시글에 달린 댓글들을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommentResponse.FindAllDTO.class)))
    })
    @GetMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<CommentResponse.FindAllDTO> readComments(
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(hidden = true) @PageableDefault(size = 10) final Pageable pageable
    ){
        final CommentResponse.FindAllDTO response = readCommentService.getComments(postId, pageable);

        return ApiResult.success(response);
    }

    // (기능) 대댓글 목록 조회
    @Operation(summary = "대댓글 목록 조회", description = "해당 postId와 commentId에 해당하는 댓글에 달린 대댓글들을 조회한다.")
    @GetMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<CommentResponse.FindAllDTO> readSubComments(
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(description = "댓글의 PK") @PathVariable final long commentId,
            @Parameter(hidden = true) @PageableDefault(size = 10) final Pageable pageable
    ){
        final CommentResponse.FindAllDTO response =
                readCommentService.getSubComments(postId, commentId, pageable);

        return ApiResult.success(response);
    }

    // (기능) 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "해당 postId와 commentId에 해당하는 댓글을 삭제한다. 대댓글 또한 동일한 URI를 이용하여 삭제한다.")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> deleteComment(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(description = "댓글의 PK") @PathVariable final long commentId
    ){
        final Long memberId = userDetails.getId();
        deleteCommentService.deleteComment(memberId, postId, commentId);

        return ApiResult.success(null);
    }
}