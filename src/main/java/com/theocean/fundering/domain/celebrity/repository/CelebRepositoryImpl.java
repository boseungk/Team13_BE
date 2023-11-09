package com.theocean.fundering.domain.celebrity.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.account.domain.QAccount;
import com.theocean.fundering.domain.celebrity.domain.QCelebrity;
import com.theocean.fundering.domain.celebrity.domain.QFollow;
import com.theocean.fundering.domain.celebrity.dto.CelebResponse;
import com.theocean.fundering.global.utils.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Objects;

import static com.theocean.fundering.domain.account.domain.QAccount.*;
import static com.theocean.fundering.domain.celebrity.domain.QCelebrity.celebrity;
import static com.theocean.fundering.domain.celebrity.domain.QFollow.*;
import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
@Slf4j
public class CelebRepositoryImpl implements CelebRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CelebResponse.FundingDTO> findAllPosting(final Long celebId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(celebId, "celebId must not be null");
        final List<CelebResponse.FundingDTO> contents = queryFactory
                .select(Projections.constructor(CelebResponse.FundingDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.title,
                        post.introduction,
                        post.participants,
                        post.targetPrice,
                        post.thumbnail
                ))
                .from(post)
                .where(eqPostCelebId(celebId), ltPostId(postId), eqCelebApprovalStatus())
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public List<CelebResponse.ListDTO> findAllCeleb(final Long celebId, final String keyword, final Pageable pageable) {
        Objects.requireNonNull(celebId, "celebId must not be null");
        if(null != keyword){
            return queryFactory
                    .selectDistinct(Projections.fields(CelebResponse.ListDTO.class,
                            celebrity.celebId,
                            celebrity.celebName,
                            celebrity.celebGender,
                            celebrity.celebCategory,
                            celebrity.celebGroup,
                            celebrity.profileImage,
                            celebrity.followerCount,
                            post.postId
                    ))
                    .from(celebrity)
                    .leftJoin(celebrity.post, post)
                    .where(ltCelebId(celebId), eqCelebApprovalStatus(), nameCondition(keyword).or(groupCondition(keyword)))
                    .orderBy(celebrity.celebId.desc())
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
                        celebrity.followerCount,
                        post.postId
                ))
                .from(celebrity)
                .leftJoin(celebrity.post, post)
                .where(ltCelebId(celebId), eqCelebApprovalStatus())
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Slice<CelebResponse.ListForApprovalDTO> findAllCelebForApproval(final Long celebId, final Pageable pageable) {
        Objects.requireNonNull(celebId, "celebId must not be null");
        final List<CelebResponse.ListForApprovalDTO> contents = queryFactory
                .select(Projections.constructor(CelebResponse.ListForApprovalDTO.class,
                        celebrity.celebId,
                        celebrity.celebName,
                        celebrity.celebGender,
                        celebrity.celebCategory,
                        celebrity.celebGroup,
                        celebrity.profileImage))
                .from(celebrity)
                .where(ltCelebId(celebId), eqCelebPendingStatus())
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
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

    private BooleanExpression ltCelebId(final Long cursorId){
        return null != cursorId ? celebrity.celebId.lt(cursorId) : null;
    }

    private BooleanExpression ltPostId(final Long cursorId){
        return null != cursorId ? post.postId.lt(cursorId) : null;
    }

    private BooleanExpression nameCondition(final String nameCond){
        return null != nameCond ? celebrity.celebName.contains(nameCond) : null;
    }

    private BooleanExpression groupCondition(final String nameCond){
        return null != nameCond ? celebrity.celebGroup.contains(nameCond) : null;
    }
}