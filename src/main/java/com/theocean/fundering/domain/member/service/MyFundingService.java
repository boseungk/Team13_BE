package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.member.dto.MyFundingFollowingCelebsDTO;
import com.theocean.fundering.domain.member.dto.MyFundingHostResponseDTO;
import com.theocean.fundering.domain.member.dto.MyFundingWithdrawalResponseDTO;
import com.theocean.fundering.domain.member.dto.MyFundingSupporterResponseDTO;
import com.theocean.fundering.domain.member.repository.MyFundingRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyFundingService {

    private final MyFundingRepository myFundingRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CelebRepository celebRepository;
    private final AdminRepository adminRepository;

    public PageResponse<MyFundingHostResponseDTO> findAllPostingByHost(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHost(userId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingSupporterResponseDTO> findAllPostingBySupporter(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingBySupporter(userId, pageable);
        return new PageResponse<>(page);
    }

    public String getNickname(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return member.getNickname();
    }

    public List<MyFundingFollowingCelebsDTO> findFollowingCelebs(final Long userId) {
        final List<MyFundingFollowingCelebsDTO> responseDTO = new ArrayList<>();
        final List<Long> allFollowingCelebId = followRepository.findAllFollowingCelebById(userId);
        for (final Long celebId : allFollowingCelebId) {
            final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                    () -> new Exception400("셀럽을 찾을 수 없습니다.")
            );
            final int followerCount = followRepository.countByCelebId(celebId);
            responseDTO.add(MyFundingFollowingCelebsDTO.of(celebrity, followerCount));
        }
        return responseDTO;
    }

    public PageResponse<MyFundingWithdrawalResponseDTO> findAllWithdrawal(final Long userId, final Pageable pageable) {
        final Long postId = adminRepository.findByUserId(userId);
        final var page = myFundingRepository.findAllWithdrawalByUser(userId, postId, pageable);
        return new PageResponse<>(page);
    }

    public void applyWithdrawal(final Long userId, final Long postId) {
        myFundingRepository.applyWithdrawal(userId, postId);
    }
}