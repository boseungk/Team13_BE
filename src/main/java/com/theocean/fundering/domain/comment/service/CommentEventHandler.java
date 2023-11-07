package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CommentEventHandler {

    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;

    @Async
    @EventListener
    public void handleCommentCreated(final CommentEvent.CreatedEvent event) {
        // 대댓글 카운트를 계산합니다.
        final int replyCount = commentRepository.countReplies(event.getPostId(), event.getParentCommentOrder() + "%.%");
        // 캐시를 업데이트합니다.
        Objects.requireNonNull(cacheManager.getCache("replyCounts")).put(event.getPostId() + "_" + event.getParentCommentOrder(), replyCount);
    }
}
