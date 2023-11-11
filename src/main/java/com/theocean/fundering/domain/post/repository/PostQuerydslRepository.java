package com.theocean.fundering.domain.post.repository;

import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostQuerydslRepository {
    Slice<PostResponse.FindAllDTO> findAllInfiniteScroll(Pageable pageable);

    Slice<PostResponse.FindAllDTO> findAllByWriterName(String nickname, Pageable pageable);

    Slice<PostResponse.FindAllDTO> findAllByKeyword(String keyword, Pageable pageable);
}
