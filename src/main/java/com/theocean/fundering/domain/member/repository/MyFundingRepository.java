package com.theocean.fundering.domain.member.repository;

import com.theocean.fundering.domain.member.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.member.dto.MyFundingWithdrawalResponseDTO;
import com.theocean.fundering.domain.member.dto.MyFundingSupporterResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface MyFundingRepository {
    Slice<MyFundingHostResponseDTO> findAllPostingByHost(Long userId, Pageable pageable);
    Slice<MyFundingSupporterResponseDTO> findAllPostingBySupporter(Long userId, Pageable pageable);
    Slice<MyFundingWithdrawalResponseDTO> findAllWithdrawalByUser(Long userId, Long postId, Pageable pageable);

}