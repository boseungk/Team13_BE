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
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<MyFundingWithdrawalResponseDTO> findAwaitingApprovalWithdrawals(final Long userId, final Pageable pageable) {
        final List<MyFundingWithdrawalResponseDTO> responseDTO = new ArrayList<>();
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        for (final Long postId : postIdList) {
            final List<Withdrawal> withdrawalList = withdrawalRepository.findWithdrawalByPostId(postId);
            if(null != withdrawalList){
                //N+1 문제 발생 가능성 쿼리 수정 필요
                final Post post = postRepository.findById(postId).orElseThrow(
                        () -> new Exception400("게시물을 찾을 수 없습니다.")
                );
                withdrawalList.stream()
                        .map(withdrawal -> MyFundingWithdrawalResponseDTO.of(withdrawal, post))
                        .forEachOrdered(responseDTO::add);
            }
        }
        return responseDTO;
    }

    public void approvalWithdrawal(final Long userId, final Long postId, final Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        final boolean isAdmin = postIdList.stream().anyMatch(id -> id.equals(postId));
        if(isAdmin == false)
            throw new Exception400("관리자가 아닙니다.");
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(
                () -> new Exception400("출금 신청을 찾을 수 없습니다.")
        );
        try{
            withdrawal.approveWithdrawal();
            withdrawalRepository.save(withdrawal);
        }catch (RuntimeException e){
            throw new Exception500("출금 신청 중 오류가 발생했습니다.");
        }
    }

    public void rejectWithdrawal(Long userId, Long postId, Long withdrawalId) {
        final List<Long> postIdList = adminRepository.findByUserId(userId);
        final boolean isAdmin = postIdList.stream().anyMatch(id -> id.equals(postId));
        if(isAdmin == false)
            throw new Exception400("관리자가 아닙니다.");
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId).orElseThrow(
                () -> new Exception400("출금 신청을 찾을 수 없습니다.")
        );
        try{
            withdrawal.denialWithdrawal();
            withdrawalRepository.save(withdrawal);
        }catch (RuntimeException e){
            throw new Exception500("출금 신청 중 오류가 발생했습니다.");
        }
    }
}