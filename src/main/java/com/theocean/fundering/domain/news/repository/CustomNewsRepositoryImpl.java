package com.theocean.fundering.domain.news.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.news.domain.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.theocean.fundering.domain.news.domain.QNews.news;

@RequiredArgsConstructor
@Repository
public class CustomNewsRepositoryImpl implements CustomNewsRepository {

    private static final long DEFAULT_CURSOR = 0;
    private final JPAQueryFactory queryFactory;

    public List<News> getNewsList(final long postId, final long cursor, final int pageSize) {
        BooleanExpression whereCondition = news.postId.eq(postId);
        if (DEFAULT_CURSOR < cursor) {
            whereCondition = whereCondition.and(news.newsId.lt(cursor));
        }

        return queryFactory
                .selectFrom(news)
                .where(whereCondition)
                .orderBy(news.newsId.desc())
                .limit(pageSize)
                .fetch();
    }
}
