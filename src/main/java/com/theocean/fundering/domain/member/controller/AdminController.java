package com.theocean.fundering.domain.member.controller;

import com.theocean.fundering.domain.member.dto.AdminResponse;
import com.theocean.fundering.domain.member.service.AdminService;
import com.theocean.fundering.global.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ADMIN", description = "펀딩 공동 관리자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminContorller {

    private final AdminService adminService;

    @Operation(summary = "공동 관리자 조회", description = "해당 postId에 대응되는 게시글의 공동관리자들을 조회한다.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AdminResponse.FindAllDTO.class)))
    })
    @GetMapping("/posts/{postId}/admins")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<List<AdminResponse.FindAllDTO>> getAdmins(@Parameter(description = "게시글의 PK") @PathVariable final long postId){

        final List<AdminResponse.FindAllDTO> response = adminService.getAdmins(postId);

        return ApiResult.success(response);
    }
}
