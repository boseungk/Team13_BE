package com.theocean.fundering.domain.celebrity.controller;

import com.theocean.fundering.domain.celebrity.dto.CelebDetailsResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebFundingResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebListResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebRequestDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebsRecommendDTO;
import com.theocean.fundering.domain.celebrity.service.CelebService;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CelebController {
    private final CelebService celebService;

    @PostMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> registerCeleb(@RequestBody @Valid final CelebRequestDTO celebRequestDTO, final Error error) {
        celebService.register(celebRequestDTO);
        return ApiResult.success(null);
    }


    @GetMapping("/celebs/{celebId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllPosting(@PathVariable final Long celebId,
                                       @RequestParam final Long postId,
                                       @PageableDefault final Pageable pageable) {
        final PageResponse<CelebFundingResponseDTO> page = celebService.findAllPosting(celebId, postId, pageable);
        return ApiResult.success(page);
    }

    @GetMapping("/celebs/{celebId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findByCelebId(@PathVariable final Long celebId) {
        final CelebDetailsResponseDTO responseDTO = celebService.findByCelebId(celebId);
        return ApiResult.success(responseDTO);
    }

    @GetMapping("/celebs")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> findAllCelebs(@RequestParam final Long celebId,
                                      @RequestParam final String keyword,
                                      @PageableDefault final Pageable pageable) {
        final PageResponse<CelebListResponseDTO> page = celebService.findAllCeleb(celebId, keyword, pageable);
        return ApiResult.success(page);
    }

    @GetMapping("/celebs/recommend")
    public ApiResult<?> findAllRecommendCelebs(
            @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        final List<CelebsRecommendDTO> responseDTO = celebService.recommendCelebs(userDetails);
        return ApiResult.success(responseDTO);
    }

}
