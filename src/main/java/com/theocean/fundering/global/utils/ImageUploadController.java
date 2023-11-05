package com.theocean.fundering.global.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageUploadController {

    private final AWSS3Uploader awss3Uploader;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/uploadImg")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> uploadImage(@RequestPart("image") final MultipartFile img) {
        final String result = awss3Uploader.uploadToS3(img);
        return ApiResult.success(result);
    }
}
