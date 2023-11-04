package com.theocean.fundering.domain.myfunding.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.myfunding.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.myfunding.repository.MyFundingRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyFundingService {
    private final MyFundingRepository myFundingRepository;
    private final MemberRepository memberRepository;
    public PageResponse<MyFundingHostResponseDTO> findAllPostingByHost(final Long userId, final Long postId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHost(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingSupporterResponseDTO> findAllPostingBySupporter(final Long userId, final Long postId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingBySupporter(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public String getNickname(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return member.getNickname();
    }
}
