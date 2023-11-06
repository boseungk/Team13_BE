package com.theocean.fundering.domain.post.controller;


import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.service.PostService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAll(@RequestParam(value = "postId", required = false) final Long postId, final Pageable pageable){
        final var responseDTO = postService.findAll(postId, pageable);
        return ApiResult.success(responseDTO);
    }

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findByPostId(@AuthenticationPrincipal final CustomUserDetails userDetails, @PathVariable final Long postId){
        String memberEmail = null;
        if (null != userDetails)
            memberEmail = userDetails.getEmail();
        final var responseDTO = postService.findByPostId(memberEmail, postId);

        return ApiResult.success(responseDTO);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/posts/{postId}/introduction")
    public ApiResult<?> postIntroduction(@PathVariable final Long postId){
        final String introduction = postService.getIntroduction(postId);
        return ApiResult.success(introduction);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/write")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> writePost(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                  @ModelAttribute final PostRequest.PostWriteDTO postWriteDTO,
                                  @RequestPart("thumbnail") final MultipartFile thumbnail){
        final String writerEmail = userDetails.getEmail();
        postService.writePost(writerEmail, postWriteDTO, thumbnail);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/posts/{postId}/edit")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> editPost(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                      @PathVariable final Long postId,
                                      @RequestBody final PostRequest.PostEditDTO postEditDTO,
                                      @RequestPart(value = "thumbnail", required = false) final MultipartFile thumbnail){
        final Long editedPost = postService.editPost(postId, postEditDTO, thumbnail);
        return ApiResult.success(editedPost);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/posts/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> deletePost(@PathVariable final Long postId){
        postService.deletePost(postId);
        return ApiResult.success(null);
    }

    @GetMapping("/posts/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> searchPost(@RequestParam(value = "postId", required = false) final Long postId, @RequestParam("keyword") final String keyword, final Pageable pageable){
        final var response = postService.searchPost(postId, keyword, pageable);
        return ApiResult.success(response);
    }

}
