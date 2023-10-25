package com.theocean.fundering.domain.post.repository;

import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface PostQuerydslRepository {
    List<PostResponse.FindAllDTO> findAll(@Nullable Long postId, String condition);

    List<PostResponse.FindAllDTO> findAllByWriterId(@Nullable Long postId, Long writerId);
    List<PostResponse.FindAllDTO> findAllByKeyword(@Nullable Long postId, String keyword);
}
