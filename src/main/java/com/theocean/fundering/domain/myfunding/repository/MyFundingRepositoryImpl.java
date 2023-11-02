package com.theocean.fundering.domain.myfunding.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.celebrity.dto.CelebFundingResponseDTO;
import com.theocean.fundering.domain.myfunding.dto.MyFundingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Objects;

import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
public class MyFundingRepositoryImpl implements MyFundingRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<MyFundingResponseDTO> findAllPostingByHost(final Long userId, final Long postId, final Pageable pageable) {
        Objects.requireNonNull(postId, "postId must not be null");
        final List<MyFundingResponseDTO> contents = queryFactory.select(Projections.constructor(MyFundingResponseDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.title,
                        post.introduction,
                        post.participants,
                        post.targetPrice))
                .from(post)
                .where()
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        final boolean hasNext = contents.size() > pageable.getPageSize();
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private BooleanExpression eqPostCelebId(final Long userId){
        return post.writer.userId.eq(userId);
    }
}
