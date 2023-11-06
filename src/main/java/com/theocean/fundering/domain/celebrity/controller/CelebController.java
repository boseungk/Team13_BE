package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.dto.CelebRequestDTO;
import com.theocean.fundering.domain.celebrity.service.CelebService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CelebController {
    private final CelebService celebService;
    @PostMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> registerCeleb(@RequestBody @Valid final CelebRequestDTO celebRequestDTO, final Error error){
//                                                @RequestPart(value = "thumbnail") MultipartFile thumbnail){
//        celebService.register(celebRequestDTO, thumbnail);
        celebService.register(celebRequestDTO);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllCelebForApproval(@PathVariable final Long celebId,
                                                     @PageableDefault final Pageable pageable
    ){
        final var page = celebService.findAllCelebForApproval(celebId, pageable);
        return ApiResult.success(page);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> approvalCelebrity(@PathVariable final Long celebId){
        celebService.approvalCelebrity(celebId);
        return ApiResult.success(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/celebs/{celebId}/admin")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> rejectCelebrity(@PathVariable final Long celebId){
        celebService.deleteCelebrity(celebId);
        return ApiResult.success(null);
    }

    @GetMapping("/celebs/{celebId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPosting(@PathVariable final Long celebId,
                                            @PageableDefault final Pageable pageable){
        final var page = celebService.findAllPosting(celebId, pageable);
        return ApiResult.success(page);
    }

    @GetMapping("/celebs/{celebId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findByCelebId(@PathVariable final Long celebId){
        final var responseDTO = celebService.findByCelebId(celebId);
        return ApiResult.success(responseDTO);
    }

    @GetMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllCelebs(@RequestParam final Long celebId,
                                           @RequestParam final String keyword,
                                           @PageableDefault final Pageable pageable){
        final var page = celebService.findAllCeleb(celebId, keyword, pageable);
        return ApiResult.success(page);
    }

    @GetMapping("/celebs/recommend")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllRecommendCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ){
        final var responseDTO = celebService.recommendCelebs(userDetails);
        return ApiResult.success(responseDTO);
    }

}