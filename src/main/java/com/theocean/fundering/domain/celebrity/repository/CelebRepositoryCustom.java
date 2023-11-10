package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.dto.CelebResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CelebRepositoryCustom {
    Slice<CelebResponse.FundingDTO> findAllPosting(Long celebId, final Long postId, Pageable pageable);

    List<CelebResponse.ListDTO> findAllCeleb(Long celebId, String keyword, Pageable pageable);

    Slice<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(Long celebId, Pageable pageable);
}
