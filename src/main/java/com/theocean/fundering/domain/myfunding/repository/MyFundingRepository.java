package com.theocean.fundering.domain.myfunding.repository;

import com.theocean.fundering.domain.myfunding.dto.MyFundingResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFundingRepository {
    Slice<MyFundingResponseDTO> findAllPostingByHost(Long userId, Long postId, Pageable pageable);
}
