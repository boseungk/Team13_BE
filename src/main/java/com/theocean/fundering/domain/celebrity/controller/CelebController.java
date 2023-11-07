package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.dto.*;
import com.theocean.fundering.domain.celebrity.service.CelebService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "CELEB", description = "celeb 관련 API")
@RequiredArgsConstructor
@RestController
public class CelebController {
    private final CelebService celebService;

    @Operation(summary = "셀럽 등록", description = "새로운 셀럽을 등록 신청한다.")
    @PostMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> registerCeleb(
            @RequestBody @Valid final CelebRequestDTO celebRequestDTO,
            @Parameter(hidden = true) final Error error){
//                                                @RequestPart(value = "thumbnail") MultipartFile thumbnail){
//        celebService.register(celebRequestDTO, thumbnail);
        celebService.register(celebRequestDTO);
        return ApiResult.success(null);
    }

    @Operation(summary = "셀럽 등록 요청 조회", description = "관리자 유저가 셀럽 등록 요청을 조회한다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllCelebForApproval(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId,
            @Parameter(hidden = true) @PageableDefault final Pageable pageable
    ){
        final var page = celebService.findAllCelebForApproval(celebId, pageable);
        return ApiResult.success(page);
    }

    @Operation(summary = "셀럽 승인", description = "관리자 유저가 셀럽 등록 요청을 승인한다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> approvalCelebrity(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId
    ){
        celebService.approvalCelebrity(celebId);
        return ApiResult.success(null);
    }

    @Operation(summary = "셀럽 거절", description = "관리자 유저가 셀럽 등록 요청을 거절한다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> rejectCelebrity(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId
    ){
        celebService.deleteCelebrity(celebId);
        return ApiResult.success(null);
    }

    @Operation(summary = "셀럽의 펀딩 조회", description = "셀럽의 id를 기반으로 펀딩을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CelebFundingResponseDTO.class)))
    })
    @GetMapping("/celebs/{celebId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPosting(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId,
            @Parameter(hidden = true) @PageableDefault final Pageable pageable
    ){
        final var page = celebService.findAllPosting(celebId, pageable);
        return ApiResult.success(page);
    }

    @Operation(summary = "셀럽의 상세 정보 조회", description = "셀럽의 id를 기반으로 상세 정보를 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CelebDetailsResponseDTO.class)))
    })
    @GetMapping("/celebs/{celebId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findByCelebId(
            @Parameter(description = "셀럽의 PK") @PathVariable final Long celebId
    ){
        final var responseDTO = celebService.findByCelebId(celebId);
        return ApiResult.success(responseDTO);
    }

    @Operation(summary = "셀럽 목록 조회", description = "셀럽의 전체 목록을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CelebListResponseDTO.class)))
    })
    @GetMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllCelebs(
            @RequestParam final Long celebId,
            @RequestParam final String keyword,
            @Parameter(hidden = true) @PageableDefault final Pageable pageable
    ){
        final var page = celebService.findAllCeleb(celebId, keyword, pageable);
        return ApiResult.success(page);
    }

    @Operation(summary = "추천 셀럽 조회", description = "추천 셀럽의 정보를 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CelebsRecommendResponseDTO.class)))
    })
    @GetMapping("/celebs/recommend")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllRecommendCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        final var responseDTO = celebService.recommendCelebs(userDetails);
        return ApiResult.success(responseDTO);
    }

}