package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.dto.CelebRequest;
import com.theocean.fundering.domain.celebrity.dto.CelebResponse;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.domain.post.repository.HeartRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.domain.post.service.PostService;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.ErrorCode;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CelebService {
    private static final int FOLLOW_COUNT_ZERO = 0;
    private static final int HEART_COUNT_ZERO = 0;
    private static final int FUNDING_AMOUNT_ZERO = 0;
    private static final long DEFAULT_MEMBER_ID = 0;

    private final CelebRepository celebRepository;

    private final PostRepository postRepository;

    private final FollowRepository followRepository;

    private final AccountRepository accountRepository;

    private final HeartRepository heartRepository;

    private final AWSS3Uploader awss3Uploader;

    @Transactional
    public void register(final CelebRequest.SaveDTO celebRequestDTO, final MultipartFile thumbnail) {
        final String img = uploadImage(thumbnail);
        final Celebrity celebrity = celebRequestDTO.mapToEntity();
        celebrity.updateProfileImage(img);
        celebRepository.save(celebrity);
    }

    @Transactional
    public void approvalCelebrity(final Long celebId) {
        final Celebrity celebrity = celebRepository.findById(celebId)
                .map(Celebrity::approvalCelebrity)
                .orElseThrow(() -> new Exception400(ErrorCode.ER02));
        celebRepository.save(celebrity);
    }

    @Transactional
    public void deleteCelebrity(final Long celebId) {
        final Celebrity celebrity = celebRepository.findById(celebId)
                .map(Celebrity::rejectCelebrity)
                .orElseThrow(() -> new Exception400(ErrorCode.ER02));
            celebRepository.save(celebrity);
    }

    public PageResponse<CelebResponse.FundingDTO> findAllPosting(final Long celebId, final CustomUserDetails member, final Pageable pageable) {
        final Long memberId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();
        final var fundingDataDTOS = celebRepository.findAllPosting(celebId, pageable);
        final List<CelebResponse.FundingDTO> fundingDtoList = new ArrayList<>();
        for (final CelebResponse.FundingDataDTO fundingDataDTO : fundingDataDTOS) {
            final boolean isHeart = HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(fundingDataDTO.getPostId(), memberId);
            final Account account = accountRepository.findByPostId(fundingDataDTO.getPostId()).orElseThrow(
                    () -> new Exception400(ErrorCode.ER08)
            );
            final boolean isWriter = fundingDataDTO.getWriterId().equals(memberId) ;
            final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebId, memberId);

            fundingDtoList.add(CelebResponse.FundingDTO.of(fundingDataDTO, account.getBalance(), isWriter, isFollow, isHeart));
        }
        return new PageResponse<>(new SliceImpl<>(fundingDtoList, pageable, hasNext(fundingDtoList, pageable)));
    }

    public CelebResponse.DetailsDTO findByCelebId(final Long celebId, final CustomUserDetails member) {
        final Long memberId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();
        final Celebrity celebrity = celebRepository.findByCelebId(celebId).orElseThrow(
                () -> new Exception400(ErrorCode.ER02));
        final Integer followerRank = celebRepository.getFollowerRank(celebId);
        final List<Post> postsByCelebId = postRepository.findPostByCelebId(celebId);
        final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebId, memberId);
        final int ongoingCount = postRepository.countByPostStatus(celebId, PostStatus.ONGOING);
        if (null == postsByCelebId)
            throw new Exception400(ErrorCode.ER03);
        // postsByCelebId에서 총 펀딩금액, 펀딩 금액 등수, 진행 중인 펀딩 개수 추출하는 로직
        int fundingAmount = FUNDING_AMOUNT_ZERO;
        for (final Post post : postsByCelebId) {
            final Account account = accountRepository.findByPostId(post.getPostId()).orElseThrow(
                    () -> new Exception400(ErrorCode.ER08)
            );
            fundingAmount += account.getBalance();
        }
        return CelebResponse.DetailsDTO.of(celebrity, fundingAmount, ongoingCount, followerRank, isFollow);
    }

    public PageResponse<CelebResponse.FundingListDTO> findAllCeleb(final CustomUserDetails member, final String keyword, final Pageable pageable) {
        final List<CelebResponse.FundingListDTO> fundingList = new ArrayList<>();
        final Long memberId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();
        // cursor -> 셀럽 리스트 조회
        final List<CelebResponse.ListDTO> celebFundingList = celebRepository.findAllCeleb(keyword, pageable);

        // 각 셀럽의 id -> 각 셀럽의 여러 펀딩 가져오기
        for (final CelebResponse.ListDTO celebFunding : celebFundingList) {
            final Integer followerRank = celebRepository.getFollowerRank(celebFunding.getCelebId());
            final int followerCount = celebFunding.getFollowerCount();
            final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebFunding.getCelebId(), memberId);
            // 각 셀럽의 id와 일치하는 펀딩 && 진행 중인 펀딩 개수 세어오기
            final int ongoingCount = postRepository.countByPostStatus(celebFunding.getCelebId(), PostStatus.ONGOING);
            // 각 셀럽에 여러 펀딩의 현재 금액들의 합
            final List<Post> postList = postRepository.findPostByCelebId(celebFunding.getCelebId());
            int fundingAmount = FUNDING_AMOUNT_ZERO;
            for (final Post post : postList) {
                final Account account = accountRepository.findByPostId(post.getPostId()).orElseThrow(
                        () -> new Exception400(ErrorCode.ER08)
                );
                fundingAmount += account.getBalance();
            }
            fundingList.add(CelebResponse.FundingListDTO.of(celebFunding, fundingAmount, ongoingCount, followerRank, isFollow, followerCount));
        }
        return new PageResponse<>(new SliceImpl<>(fundingList, pageable, hasNext(fundingList, pageable)));
    }

    public PageResponse<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(final Pageable pageable) {
        final var page = celebRepository.findAllCelebForApproval(pageable);
        return new PageResponse<>(page);
    }

    public List<CelebResponse.ProfileDTO> recommendCelebs(final CustomUserDetails member) {
        final Long memberId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();

        final List<Celebrity> celebrities = celebRepository.findAllRandom();
        if (null == celebrities)
            throw new Exception400(ErrorCode.ER02);

        final List<CelebResponse.ProfileDTO> responseDTO = new ArrayList<>();
        for (final Celebrity celebrity : celebrities) {
            final int followCount = celebrity.getFollowerCount();
            final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebrity.getCelebId(), memberId);
            responseDTO.add(CelebResponse.ProfileDTO.of(celebrity, followCount, isFollow));
        }
        return responseDTO;
    }

    private String uploadImage(final MultipartFile img) {
        return awss3Uploader.uploadToS3(img);
    }

    private boolean hasNext(final List<?> contents, final Pageable pageable){
        if (contents.size() > pageable.getPageSize()) {
            contents.remove(contents.size() - 1);
            return true;
        }
        return false;
    }
}