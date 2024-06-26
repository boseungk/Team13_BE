package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
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
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private final AccountRepository accountRepository;

    public PageResponse<MyFundingResponse.HostDTO> findAllPostingByHost(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHost(userId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingResponse.SupporterDTO> findAllPostingBySupporter(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingBySupporter(userId, pageable);
        return new PageResponse<>(page);
    }

    public PageResponse<MyFundingResponse.HeartPostingDTO> findAllPostingByHeart(final Long userId, final Pageable pageable) {
        final var page = myFundingRepository.findAllPostingByHeart(userId, pageable);
        return new PageResponse<>(page);
    }

    public MyFundingResponse.EmailDTO getNickname(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400(ErrorCode.ER01)
        );
        return MyFundingResponse.EmailDTO.from(member.getNickname());
    }

    public List<MyFundingResponse.FollowingCelebsDTO> findFollowingCelebs(final Long userId) {
        return followRepository.findAllFollowingCelebById(userId)
                .stream()
                .map(celebId -> MyFundingResponse.FollowingCelebsDTO.of(getCelebrity(celebId), getFollowerCount(celebId)))
                .collect(Collectors.toList());
    }

    private Celebrity getCelebrity(final Long celebId) {
        return celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400(ErrorCode.ER02)
        );
    }

    private int getFollowerCount(final Long celebId) {
        return followRepository.countByCelebId(celebId);
    }

    public List<MyFundingResponse.WithdrawalDTO> findAwaitingApprovalWithdrawals(final Long userId, final Pageable pageable) {
        return adminRepository.findByMemberId(userId).stream()
                .map(this::getWithdrawalDTOsByPostId)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<MyFundingResponse.WithdrawalDTO> getWithdrawalDTOsByPostId(final Long postId) {
        if (null == getWithdrawalByPostId(postId))
            return Collections.emptyList();
        return getWithdrawalByPostId(postId).stream()
                .map(withdrawal -> MyFundingResponse.WithdrawalDTO.of(withdrawal, getPost(postId)))
                .collect(Collectors.toList());
    }

    private List<Withdrawal> getWithdrawalByPostId(final Long postId) {
        return withdrawalRepository.findWithdrawalByPostId(postId);
    }


    private Post getPost(final Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new Exception400(ErrorCode.ER03)
        );
    }

    @Transactional
    public void approvalWithdrawal(final Long userId, final Long postId, final Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByMemberId(userId);
        if (!isAdmin(postId, postIdList))
            throw new Exception400(ErrorCode.ER17);
        final int balanceAfterWithdrawal = calculateBalanceAfterWithdrawal(postId, withdrawalId);
        getWithdrawal(withdrawalId).approveWithdrawal(balanceAfterWithdrawal);
        getAccount(postId).updateBalance(balanceAfterWithdrawal);
    }

    private int calculateBalanceAfterWithdrawal(Long postId, Long withdrawalId) {
        return getAccount(postId).getBalance() - getWithdrawal(withdrawalId).getWithdrawalAmount();
    }

    private Withdrawal getWithdrawal(Long withdrawalId) {
        return withdrawalRepository.findById(withdrawalId).orElseThrow(
                () -> new Exception400(ErrorCode.ER14)
        );
    }

    private Account getAccount(Long postId) {
        return accountRepository.findByPostId(postId).orElseThrow(
                () -> new Exception400(ErrorCode.ER08)
        );
    }

    @Transactional
    public void rejectWithdrawal(final Long userId, final Long postId, final Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByMemberId(userId);
        if (!isAdmin(postId, postIdList))
            throw new Exception400(ErrorCode.ER17);
        final Withdrawal withdrawal = getWithdrawal(withdrawalId);
        withdrawal.denyWithdrawal();
        withdrawalRepository.save(withdrawal);
    }

    private boolean isAdmin(final Long postId, final List<Long> postIdList) {
        return postIdList.stream().anyMatch(id -> id.equals(postId));
    }

}