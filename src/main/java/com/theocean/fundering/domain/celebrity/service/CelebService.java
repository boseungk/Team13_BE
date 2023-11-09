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
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.dto.PageResponse;
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
    private static final int FUNDING_AMOUNT_ZERO = 0;
    private static final long DEFAULT_MEMBER_ID = 0;

    private final CelebRepository celebRepository;

    private final PostRepository postRepository;

    private final FollowRepository followRepository;

    private final AccountRepository accountRepository;

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
                .orElseThrow(() -> new Exception400("해당 셀럽을 찾을 수 없습니다."));
        celebRepository.save(celebrity);
    }

    @Transactional
    public void deleteCelebrity(final Long celebId) {
        final Celebrity celebrity = celebRepository.findById(celebId)
                .map(Celebrity::rejectCelebrity)
                .orElseThrow(() -> new Exception400("해당 셀럽을 찾을 수 없습니다."));
            celebRepository.save(celebrity);
    }

    public PageResponse<CelebResponse.FundingDTO> findAllPosting(final Long celebId, final Long postId, final Pageable pageable) {
        final var page = celebRepository.findAllPosting(celebId, postId, pageable);
        return new PageResponse<>(page);
    }

    public CelebResponse.DetailsDTO findByCelebId(final Long celebId) {
        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다."));
        final int followerCount = celebrity.getFollowerCount();
        final int followerRank = celebRepository.getFollowerRank(celebId);
        final List<Post> postsByCelebId = postRepository.findPostByCelebId(celebId);
        if (null == postsByCelebId)
            throw new Exception400("관련 포스팅을 찾을 수 없습니다.");
        // postsByCelebId에서 총 펀딩금액, 펀딩 금액 등수, 진행 중인 펀딩 개수 추출하는 로직
        return CelebResponse.DetailsDTO.of(celebrity, followerCount, followerRank, postsByCelebId);
    }

    public PageResponse<CelebResponse.FundingListDTO> findAllCeleb(final Long celebId, final CustomUserDetails member, final String keyword, final Pageable pageable) {
        int fundingAmount = FUNDING_AMOUNT_ZERO;
        final List<CelebResponse.FundingListDTO> fundingList = new ArrayList<>();
        final Long userId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();
        final List<CelebResponse.ListDTO> celebFundingList = celebRepository.findAllCeleb(celebId, keyword, pageable);
        final int ongoingCount = postRepository.countByPostStatus(PostStatus.ONGOING);
        final int followerRank = celebRepository.getFollowerRank(celebId);
        final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebId, userId);

        for (final CelebResponse.ListDTO celebFunding : celebFundingList) {
            final Account account = accountRepository.findByPostId(celebFunding.getPostId()).orElseThrow(
                    () -> new Exception400("계좌를 찾을 수 없습니다.")
            );
            fundingAmount += account.getBalance();
            fundingList.add(CelebResponse.FundingListDTO.of(celebFunding, fundingAmount, ongoingCount, followerRank, isFollow));
        }
        final boolean hasNext = fundingList.size() > pageable.getPageSize();
        return new PageResponse<>(new SliceImpl<>(fundingList, pageable, hasNext));
    }

    public PageResponse<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(final Long celebId, final Pageable pageable) {
        final var page = celebRepository.findAllCelebForApproval(celebId, pageable);
        return new PageResponse<>(page);
    }

    public List<CelebResponse.ProfileDTO> recommendCelebs(final CustomUserDetails member) {
        final Long userId = (null == member) ? DEFAULT_MEMBER_ID : member.getId();

        final List<Celebrity> celebrities = celebRepository.findAllRandom();
        if (null == celebrities)
            throw new Exception400("해당 셀럽을 찾을 수 없습니다.");

        final List<CelebResponse.ProfileDTO> responseDTO = new ArrayList<>();
        for (final Celebrity celebrity : celebrities) {
            final int followCount = celebrity.getFollowerCount();
            final boolean isFollow = FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebrity.getCelebId(), userId);
            responseDTO.add(CelebResponse.ProfileDTO.of(celebrity, followCount, isFollow));
        }
        return responseDTO;
    }

    private String uploadImage(final MultipartFile img) {
        return awss3Uploader.uploadToS3(img);
    }
}