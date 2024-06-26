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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CelebService {
    private static final int FOLLOW_COUNT_ZERO = 0;
    private static final int HEART_COUNT_ZERO = 0;
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
        final List<CelebResponse.FundingDTO> fundingDtoList = celebRepository.findAllPosting(celebId, pageable)
                .stream()
                .map(fundingDataDTO -> CelebResponse.FundingDTO.of(
                        fundingDataDTO,
                        findAccountByPostId(fundingDataDTO.getPostId()).getBalance(),
                        isMemberTheWriter(fundingDataDTO, getMemberIdOrDefault(member)),
                        isMemberFollowingCeleb(celebId, getMemberIdOrDefault(member)),
                        isMemberGivenHeart(fundingDataDTO, getMemberIdOrDefault(member))))
                .collect(Collectors.toList());
        return new PageResponse<>(new SliceImpl<>(fundingDtoList, pageable, hasNext(fundingDtoList, pageable)));
    }

    private long getMemberIdOrDefault(final CustomUserDetails member) {
        return (null == member) ? DEFAULT_MEMBER_ID : member.getId();
    }

    private Account findAccountByPostId(final Long fundingDataDTO) {
        return accountRepository.findByPostId(fundingDataDTO).orElseThrow(
                () -> new Exception400(ErrorCode.ER08)
        );
    }

    private boolean isMemberTheWriter(final CelebResponse.FundingDataDTO fundingDataDTO, final Long memberId) {
        return fundingDataDTO.getWriterId().equals(memberId);
    }

    private boolean isMemberFollowingCeleb(final Long celebId, final Long memberId) {
        return FOLLOW_COUNT_ZERO != followRepository.countByCelebIdAndFollowId(celebId, memberId);
    }

    private boolean isMemberGivenHeart(final CelebResponse.FundingDataDTO fundingDataDTO, final Long memberId) {
        return HEART_COUNT_ZERO != heartRepository.countByPostIdAndHeartId(fundingDataDTO.getPostId(), memberId);
    }

    public CelebResponse.DetailsDTO findByCelebId(final Long celebId, final CustomUserDetails member) {
        if (null == getPostByCelebId(celebId))
            throw new Exception400(ErrorCode.ER03);
        return CelebResponse.DetailsDTO.of(
                getCelebrity(celebId),
                countOngoingFunding(getPostByCelebId(celebId)),
                getOngoingPostCount(celebId),
                getFollowerRank(celebId),
                isMemberFollowingCeleb(celebId, getMemberIdOrDefault(member))
        );
    }

    private List<Post> getPostByCelebId(final Long celebId) {
        return postRepository.findPostByCelebId(celebId);
    }

    private Celebrity getCelebrity(final Long celebId) {
        return celebRepository.findByCelebId(celebId).orElseThrow(
                () -> new Exception400(ErrorCode.ER02));
    }

    private int countOngoingFunding(final List<Post> postsByCelebId) {
        return postsByCelebId.stream()
                .map(post -> findAccountByPostId(post.getPostId()))
                .mapToInt(Account::getBalance)
                .reduce(0, Integer::sum);
    }

    private Integer getFollowerRank(final Long celebId) {
        return celebRepository.getFollowerRank(celebId);
    }

    private int getOngoingPostCount(final Long celebId) {
        return postRepository.countByPostStatus(celebId, PostStatus.ONGOING);
    }

    public PageResponse<CelebResponse.FundingListDTO> findAllCeleb(final CustomUserDetails member, final String keyword, final Pageable pageable) {
        final List<CelebResponse.FundingListDTO> fundingList = celebRepository.findAllCeleb(keyword, pageable)
                .stream()
                .map(celebFunding -> CelebResponse.FundingListDTO.of(
                                celebFunding,
                                sumTotalFundingForCeleb(celebFunding),
                                getOngoingPostCount(celebFunding.getCelebId()),
                                getFollowerRank(celebFunding.getCelebId()),
                                isMemberFollowingCeleb(celebFunding.getCelebId(), getMemberIdOrDefault(member)),
                                celebFunding.getFollowerCount()
                        )
                ).collect(Collectors.toList());
        return new PageResponse<>(new SliceImpl<>(fundingList, pageable, hasNext(fundingList, pageable)));
    }

    private int sumTotalFundingForCeleb(final CelebResponse.ListDTO celebFunding) {
        return getPostByCelebId(celebFunding.getCelebId()).stream()
                .map(post -> findAccountByPostId(post.getPostId()))
                .mapToInt(Account::getBalance)
                .reduce(0, Integer::sum);
    }

    public PageResponse<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(final Pageable pageable) {
        final var page = celebRepository.findAllCelebForApproval(pageable);
        return new PageResponse<>(page);
    }

    public List<CelebResponse.ProfileDTO> recommendCelebs(final CustomUserDetails member) {
        if (null == getCelebAllRandom())
            throw new Exception400(ErrorCode.ER02);
        return getCelebAllRandom().stream()
                .map(celebrity -> CelebResponse.ProfileDTO.of(
                                celebrity,
                                celebrity.getFollowerCount(),
                                isMemberFollowingCeleb(celebrity.getCelebId(), getMemberIdOrDefault(member))
                        )
                ).collect(Collectors.toList());
    }

    private List<Celebrity> getCelebAllRandom() {
        return celebRepository.findAllRandom();
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