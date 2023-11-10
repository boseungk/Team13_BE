package com.theocean.fundering.global.utils;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "AWS S3 Uploader", description = "AWS S3 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageUploadController {

    private final AWSS3Uploader awss3Uploader;

    //    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "이미지 업로드", description = "AWS S3에 이미지를 업로드 합니다.")
    @PostMapping(value = "/uploadImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> uploadImage(@Parameter(description = "이미지 파일, content-type: Multipart/Form-data") @RequestPart("image") final MultipartFile img) {
        final String result = awss3Uploader.uploadToS3(img);
        return ApiResult.success(result);
    }
}
