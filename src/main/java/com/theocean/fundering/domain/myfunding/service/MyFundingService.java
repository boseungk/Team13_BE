package com.theocean.fundering.domain.myfunding.service;

import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingManagerResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.myfunding.repository.MyFundingRepository;
import com.theocean.fundering.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyFundingService {
    private final MyFundingRepository myFundingRepository;
    public PageResponse<MyFundingHostResponseDTO> findAllPostingByHost(Long userId, Long postId, Pageable pageable) {
        var page = myFundingRepository.findAllPostingByHost(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingSupporterResponseDTO> findAllPostingBySupporter(Long userId, Long postId, Pageable pageable) {
        var page = myFundingRepository.findAllPostingBySupporter(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingManagerResponseDTO> findAllPostingByManager(Long userId, Long postId, Pageable pageable) {
        var page = myFundingRepository.findAllPostingByManager(userId, postId, pageable);
        return new PageResponse<>(page);
    }
}