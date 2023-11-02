package com.theocean.fundering.domain.myfunding.controller;

import com.theocean.fundering.domain.myfunding.dto.MyFundingResponseDTO;
import com.theocean.fundering.domain.myfunding.service.MyFundingService;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyFundingController {
    private final MyFundingService myFundingService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myfunding/host")
    public ResponseEntity<?> findAllPostingByHost(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam final Long postId,
            @PageableDefault final Pageable pageable){
        final PageResponse<MyFundingResponseDTO> page = myFundingService.findAllPostingByHost(userDetails.getId(), postId, pageable);
        return ResponseEntity.ok(ApiUtils.success(page));
    }

}
