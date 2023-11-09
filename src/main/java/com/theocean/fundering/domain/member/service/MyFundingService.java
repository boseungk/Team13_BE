package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.MyFundingResponse;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.member.repository.MyFundingRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MyFundingService {

    private final MyFundingRepository myFundingRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CelebRepository celebRepository;
    private final AdminRepository adminRepository;

    public PageResponse<MyFundingResponse.HostDTO> findAllPostingByHost(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHost(userId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingResponse.SupporterDTO> findAllPostingBySupporter(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingBySupporter(userId, pageable);
        return new PageResponse<>(page);
    }

    public MyFundingResponse.EmailDTO getNickname(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return MyFundingResponse.EmailDTO.from(member.getNickname());
    }

    public List<MyFundingResponse.FollowingCelebsDTO> findFollowingCelebs(final Long userId) {
        final List<MyFundingResponse.FollowingCelebsDTO> responseDTO = new ArrayList<>();
        final List<Long> allFollowingCelebId = followRepository.findAllFollowingCelebById(userId);
        for (final Long celebId : allFollowingCelebId) {
            final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                    () -> new Exception400("셀럽을 찾을 수 없습니다.")
            );
            final int followerCount = followRepository.countByCelebId(celebId);
            responseDTO.add(MyFundingResponse.FollowingCelebsDTO.of(celebrity, followerCount));
        }
        return responseDTO;
    }

    public List<MyFundingResponse.WithdrawalDTO> findAwaitingApprovalWithdrawals(final Long userId, final Pageable pageable) {
        final List<MyFundingResponse.WithdrawalDTO> responseDTO = new ArrayList<>();
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        for (final Long postId : postIdList) {
            final List<Withdrawal> withdrawalList = withdrawalRepository.findWithdrawalByPostId(postId);
            if (null != withdrawalList) {
                //N+1 문제 발생 가능성 쿼리 수정 필요
                final Post post = postRepository.findById(postId).orElseThrow(
                        () -> new Exception400("게시물을 찾을 수 없습니다.")
                );
                withdrawalList.stream()
                        .map(withdrawal -> MyFundingResponse.WithdrawalDTO.of(withdrawal, post))
                        .forEachOrdered(responseDTO::add);
            }
        }
        return responseDTO;
    }

    @Transactional
    public void approvalWithdrawal(final Long userId, final Long postId, final Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        final boolean isAdmin = postIdList.stream().anyMatch(id -> id.equals(postId));
        if (!isAdmin)
            throw new Exception400("관리자가 아닙니다.");
        final Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(
                () -> new Exception400("출금 신청을 찾을 수 없습니다.")
        );
        withdrawal.approveWithdrawal();
        withdrawalRepository.save(withdrawal);
    }

    @Transactional
    public void rejectWithdrawal(final Long userId, final Long postId, final Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        final boolean isAdmin = postIdList.stream().anyMatch(id -> id.equals(postId));
        if (!isAdmin)
            throw new Exception400("관리자가 아닙니다.");
        final Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(
                () -> new Exception400("출금 신청을 찾을 수 없습니다.")
        );
        withdrawal.denyWithdrawal();
        withdrawalRepository.save(withdrawal);
    }
}