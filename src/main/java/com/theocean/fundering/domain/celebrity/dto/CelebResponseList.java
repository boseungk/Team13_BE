package com.theocean.fundering.domain.celebrity.dto;

import com.theocean.fundering.domain.celebrity.domain.constant.CelebCategory;
import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@AllArgsConstructor
@RequiredArgsConstructor
public class CelebResponseList {

    private final Long celebId;
    private final String celebName;
    private final CelebGender celebGender;
    private final CelebCategory celebCategory;
    private final String celeGroup;
    private final String profileImage;
    private final int followerCount;
    private final int postId = 1;
    private final PostStatus postStatus = PostStatus.ONGOING;
}