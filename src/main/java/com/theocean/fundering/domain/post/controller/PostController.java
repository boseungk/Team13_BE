package com.theocean.fundering.domain.post.controller;


import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.service.PostService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "POST", description = "펀딩 게시물 관련 API")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @Operation(summary = "전체 게시물 조회", description = "전체 펀딩 게시물을 조회합니다.")
    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAll(@Parameter(description = "무한 스크롤 기준점") @RequestParam(value = "postId", required = false) final Long postId,
                                @Parameter(description = "page, size, sort") final Pageable pageable){
        final var responseDTO = postService.findAll(postId, pageable);
        return ApiResult.success(responseDTO);
    }

    @Operation(summary = "특정 게시물 조회", description = "게시물 pk를 기반으로 특정 펀딩 게시물을 조회합니다.")
    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findByPostId(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                     @Parameter(description = "게시물 id") @PathVariable final Long postId){
        String memberEmail = null;
        if (null != userDetails)
            memberEmail = userDetails.getEmail();
        final var responseDTO = postService.findByPostId(memberEmail, postId);

        return ApiResult.success(responseDTO);

    }

    @Operation(summary = "게시물 소개글 조회", description = "펀딩 게시물의 소개글을 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts/{postId}/introduction")
    public ApiResult<?> postIntroduction(@Parameter(description = "게시물 pk") @PathVariable final Long postId){
        final String introduction = postService.getIntroduction(postId);
        return ApiResult.success(introduction);
    }

    @Operation(summary = "펀딩 게시물 작성", description = "펀딩 게시물을 작성합니다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/posts/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> writePost(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                  @Parameter(description = "게시물 작성 DTO, content-type: application/json") @RequestPart(value = "dto") final PostRequest.PostWriteDTO postWriteDTO,
                                  @Parameter(description = "썸네일 이미지") @RequestPart(value = "thumbnail") final MultipartFile thumbnail){
        final String writerEmail = userDetails.getEmail();
        postService.writePost(writerEmail, postWriteDTO, thumbnail);
        return ApiResult.success(null);
    }

    @Operation(summary = "펀딩 게시물 수정", description = "작성한 펀딩 게시물을 수정합니다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/posts/{postId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> editPost(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                 @PathVariable final Long postId,
                                 @Parameter(description = "게시물 수정 DTO, content-type: application/json") @RequestPart(value = "dto") final PostRequest.PostEditDTO postEditDTO,
                                 @Parameter(description = "새로운 썸네일 이미지") @RequestPart(value = "thumbnail", required = false) final MultipartFile thumbnail){
        final String memberEmail = userDetails.getEmail();
        final Long editedPost = postService.editPost(postId, memberEmail, postEditDTO, thumbnail);
        return ApiResult.success(editedPost);
    }

    @Operation(summary = "펀딩 게시물 삭제", description = "펀딩 게시물을 삭제합니다.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/posts/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> deletePost(@Parameter(description = "삭제 대상 게시물 pk") @PathVariable final Long postId){
        postService.deletePost(postId);
        return ApiResult.success(null);
    }

    @Operation(summary = "펀딩 게시물 검색", description = "검색어를 기반으로 펀딩 게시물을 검색합니다.")
    @GetMapping("/posts/search/keyword")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> searchPostByKeyword(@Parameter(description = "무한 스크롤 기준점") @RequestParam(value = "postId", required = false) final Long postId,
                                            @Parameter(description = "검색어") @RequestParam("keyword") final String keyword,
                                            @Parameter(description = "page, size, sort") final Pageable pageable){
        final var response = postService.searchPostByKeyword(postId, keyword, pageable);
        return ApiResult.success(response);
    }

    @Operation(summary = "펀딩 게시물 검색", description = "사용자 닉네임을 기반으로 펀딩 게시물을 검색합니다.")
    @GetMapping("/posts/search/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> searchPostByNickname(@Parameter(description = "무한 스크롤 기준점") @RequestParam(value = "postId", required = false) final Long postId,
                                             @Parameter(description = "사용자 닉네임") @RequestParam("nickname") final String nickname,
                                             @Parameter(description = "page, size, sort") final Pageable pageable){
        final var response = postService.findAllByWriterEmail(postId, nickname, pageable);
        return ApiResult.success(response);
    }

}
