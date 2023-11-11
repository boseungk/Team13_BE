package com.theocean.fundering.domain.celebrity.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.celebrity.dto.CelebResponse;
import com.theocean.fundering.global.utils.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.theocean.fundering.domain.celebrity.domain.QCelebrity.celebrity;
import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
@Slf4j
public class CelebRepositoryImpl implements CelebRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CelebResponse.FundingDataDTO> findAllPosting(final Long celebId, final Pageable pageable) {
        final List<CelebResponse.FundingDataDTO> contents = queryFactory
                .select(Projections.constructor(CelebResponse.FundingDataDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.writer.profileImage,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.celebrity.profileImage,
                        post.title,
                        post.introduction,
                        post.thumbnail,
                        post.targetPrice,
                        post.deadline,
                        post.createdAt,
                        post.modifiedAt,
                        post.participants,
                        post.heartCount
                ))
                .from(post)
                .where(eqPostCelebId(celebId), eqCelebApprovalStatus())
                .offset(pageable.getOffset())
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNext(contents, pageable));
    }

    @Override
    public List<CelebResponse.ListDTO> findAllCeleb(final String keyword, final Pageable pageable) {
        if(null != keyword){
            return queryFactory
                    .selectDistinct(Projections.constructor(CelebResponse.ListDTO.class,
                            celebrity.celebId,
                            celebrity.celebName,
                            celebrity.celebGender,
                            celebrity.celebCategory,
                            celebrity.celebGroup,
                            celebrity.profileImage,
                            celebrity.followerCount
                    ))
                    .from(celebrity)
                    .where(eqCelebApprovalStatus(), nameCondition(keyword))
                    .offset(pageable.getOffset())
                    .orderBy(celebrity.followerCount.asc())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        return queryFactory
                .select(Projections.constructor(CelebResponse.ListDTO.class,
                        celebrity.celebId,
                        celebrity.celebName,
                        celebrity.celebGender,
                        celebrity.celebCategory,
                        celebrity.celebGroup,
                        celebrity.profileImage,
                        celebrity.followerCount
                ))
                .from(celebrity)
                .where(eqCelebApprovalStatus())
                .offset(pageable.getOffset())
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Slice<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(final Pageable pageable) {
        final List<CelebResponse.ListForApprovalDTO> contents = queryFactory
                .select(Projections.constructor(CelebResponse.ListForApprovalDTO.class,
                        celebrity.celebId,
                        celebrity.celebName,
                        celebrity.celebGender,
                        celebrity.celebCategory,
                        celebrity.celebGroup,
                        celebrity.profileImage))
                .from(celebrity)
                .where(eqCelebPendingStatus())
                .offset(pageable.getOffset())
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return new SliceImpl<>(contents, pageable, hasNext(contents, pageable));
    }

    private boolean hasNext(List<?> contents, Pageable pageable){
        if (contents.size() > pageable.getPageSize()) {
            contents.remove(contents.size() - 1);
            return true;
        }
        return false;
    }

    private BooleanExpression eqCelebApprovalStatus() {
        return celebrity.status.eq(ApprovalStatus.APPROVED);
    }

    private BooleanExpression eqCelebPendingStatus() {
        return celebrity.status.eq(ApprovalStatus.PENDING);
    }

    private BooleanExpression eqPostCelebId(final Long celebId){
        return post.celebrity.celebId.eq(celebId);
    }


    private BooleanExpression nameCondition(final String nameCond){
        return null != nameCond ? celebrity.celebName.contains(nameCond) : null;
    }

    private BooleanExpression groupCondition(final String nameCond){
        return null != nameCond ? celebrity.celebGroup.contains(nameCond) : null;
    }
}