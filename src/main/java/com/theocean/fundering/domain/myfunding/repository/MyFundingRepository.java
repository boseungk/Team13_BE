package com.theocean.fundering.domain.myfunding.repository;

import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;


public interface MyFundingRepository {
    Slice<MyFundingHostResponseDTO> findAllPostingByHost(Long userId, Long postId, Pageable pageable);
    Slice<MyFundingSupporterResponseDTO> findAllPostingBySupporter(Long userId, Long postId, Pageable pageable);
}
