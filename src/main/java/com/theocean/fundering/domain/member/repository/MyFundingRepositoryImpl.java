package com.theocean.fundering.domain.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.heart.domain.QHeart;
import com.theocean.fundering.domain.member.dto.MyFundingResponse;
import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.theocean.fundering.domain.heart.domain.QHeart.*;
import static com.theocean.fundering.domain.payment.domain.QPayment.payment;
import static com.theocean.fundering.domain.post.domain.QPost.post;
import static com.theocean.fundering.domain.withdrawal.domain.QWithdrawal.withdrawal;

@RequiredArgsConstructor
@Repository
public class MyFundingRepositoryImpl implements MyFundingRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<MyFundingResponse.HostDTO> findAllPostingByHost(final Long userId, final Pageable pageable) {
        final List<MyFundingResponse.HostDTO> contents =
                queryFactory.select(Projections.constructor(MyFundingResponse.HostDTO.class,
                                post.postId,
                                post.writer.nickname,
                                post.celebrity.celebName,
                                post.celebrity.profileImage,
                                post.title,
                                post.thumbnail,
                                post.introduction,
                                post.targetPrice,
                                post.deadline,
                                post.modifiedAt,
                                post.createdAt
                        ))
                        .from(post)
                        .where(eqPostWriterId(userId))
                        .orderBy(post.postId.desc())
                        .limit(pageable.getPageSize())
                        .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<MyFundingResponse.SupporterDTO> findAllPostingBySupporter(final Long userId, final Pageable pageable) {
        final List<MyFundingResponse.SupporterDTO> contents =
                queryFactory.select(Projections.constructor(MyFundingResponse.SupporterDTO.class,
                                post.postId,
                                post.writer.nickname,
                                post.celebrity.celebName,
                                post.celebrity.profileImage,
                                post.title,
                                post.thumbnail,
                                post.introduction,
                                post.targetPrice,
                                payment.amount,
                                post.deadline,
                                post.modifiedAt,
                                post.createdAt
                        ))
                        .from(post)
                        .leftJoin(payment).on(payment.member.userId.eq(post.postId))
                        .where(eqPostSupporterId(userId))
                        .orderBy(post.postId.desc())
                        .limit(pageable.getPageSize())
                        .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<MyFundingResponse.HeartPostingDTO> findAllPostingByHeart(final Long userId, final Long postId, final Pageable pageable) {
        final List<MyFundingResponse.HeartPostingDTO> contents =
                queryFactory.select(Projections.constructor(MyFundingResponse.HeartPostingDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.celebrity.profileImage,
                        post.title,
                        post.thumbnail,
                        post.targetPrice,
                        post.account.balance,
                        post.deadline,
                        post.createdAt,
                        post.modifiedAt,
                        post.heartCount))
                .from(post)
                .leftJoin(heart).on(heart.member.userId.eq(userId))
                .where(ltPostId(postId), eqHeart(userId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();

        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private BooleanExpression ltPostId(final Long cursorId){
        return null != cursorId ? post.postId.lt(cursorId) : null;
    }

    private BooleanExpression eqHeart(final Long userId){
        return heart.member.userId.eq(userId);
    }

    private BooleanExpression eqPostWriterId(final Long userId){
        return post.writer.userId.eq(userId);
    }

    private BooleanExpression eqPostSupporterId(final Long userId){
        return payment.member.userId.eq(userId);
    }

}