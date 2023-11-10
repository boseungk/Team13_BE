package com.theocean.fundering.domain.news.controller;

import com.theocean.fundering.domain.news.dto.NewsRequest;
import com.theocean.fundering.domain.news.dto.NewsResponse;
import com.theocean.fundering.domain.news.service.CreateNewsService;
import com.theocean.fundering.domain.news.service.ReadNewsService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "NEWS", description = "펀딩 업데이트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {

    private final CreateNewsService createNewsService;
    private final ReadNewsService readNewsService;

    // (기능) 펀딩 업데이트 작성
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "펀딩 업데이트 작성", description = "펀딩 주최자가 업데이트를 작성한다.")
    @PostMapping("/posts/{postId}/updates")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> createUpdates(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @RequestBody @Schema(implementation = NewsRequest.SaveDTO.class) final NewsRequest.SaveDTO request
    ){
        final Long writerId = userDetails.getId();
        createNewsService.createNews(writerId, postId, request);
        return ApiResult.success(null);
    }

    // (기능) 펀딩 업데이트 조회
    @Operation(summary = "펀딩 업데이트 조회", description = "펀딩 id로 업데이트를 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = NewsResponse.findAllDTO.class)))
    })
    @GetMapping("/posts/{postId}/updates")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> readUpdates(
            @Parameter(description = "게시글의 PK") @PathVariable final long postId,
            @Parameter(description = "마지막으로 조회한 업데이트 글의 PK") @RequestParam(required = false, defaultValue = "0") final long cursor,
            @Parameter(hidden = true) @RequestParam(required = false, defaultValue = "6") final int pageSize
    ){
        final var response = readNewsService.getNews(postId, cursor, pageSize);
        return ApiResult.success(response);
    }
}
