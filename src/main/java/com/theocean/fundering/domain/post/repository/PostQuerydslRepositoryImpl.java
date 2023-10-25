package com.theocean.fundering.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.post.dto.PostResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;


import static  com.theocean.fundering.domain.post.domain.QPost.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class PostQuerydslRepositoryImpl implements PostQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostResponse.FindAllDTO> findAll(@Nullable Long postId, String condition) {
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(condition);

        return jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.title,
                        post.thumbnail,
                        post.writer,
                        post.celebrity,
                        post.targetPrice,
                        post.account,
                        post.deadline,
                        post.createdAt))
                .from(post)
                .where(ltPostId(postId))
                .orderBy(orderSpecifiers)
                .limit(12)
                .fetch();
    }

    @Override
    public List<PostResponse.FindAllDTO> findAllByWriterId(@Nullable Long postId, Long writerId) {
        return jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.title,
                        post.thumbnail,
                        post.writer,
                        post.celebrity,
                        post.targetPrice,
                        post.account,
                        post.deadline,
                        post.createdAt))
                .from(post)
                .where(ltPostId(postId), eqWriter(writerId))
                .orderBy(post.postId.desc())
                .limit(12)
                .fetch();
    }
    @Override
    public List<PostResponse.FindAllDTO> findAllByKeyword(@Nullable Long postId, String keyword){
        return jpaQueryFactory
                .select(Projections.bean(PostResponse.FindAllDTO.class,
                        post.title,
                        post.thumbnail,
                        post.writer,
                        post.celebrity,
                        post.targetPrice,
                        post.account,
                        post.deadline,
                        post.createdAt))
                .from(post)
                .where(ltPostId(postId), containKeyword(keyword))
                .orderBy(post.postId.desc())
                .limit(12)
                .fetch();
    }

    private OrderSpecifier[] createOrderSpecifier(String condition){
        List<OrderSpecifier> specifiers = new ArrayList<>();

        /* 정렬 조건 추가 시 추가 작성
        * */
        if(Objects.equals(condition, "DEFAULT"))
            specifiers.add(new OrderSpecifier(Order.DESC, post.postId));
        else if(Objects.equals(condition, "DEADLINE"))
            specifiers.add(new OrderSpecifier(Order.DESC, post.deadline));
        else if(Objects.equals(condition, "AMOUNT"))
            specifiers.add(new OrderSpecifier(Order.DESC, post.account.fundingAmount));

        return specifiers.toArray(new OrderSpecifier[specifiers.size()]);
    }

    private BooleanExpression ltPostId(@Nullable Long postId){
        if (postId == null) return null;
        return post.postId.lt(postId);
    }

    private BooleanExpression eqWriter(Long writerId){
        return post.writer.userId.eq(writerId);
    }
    private BooleanExpression containKeyword(String keyword){
        return post.title.contains(keyword);
    }

}
