package com.theocean.fundering.domain.comment.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CommentEvent {
    @Getter
    @RequiredArgsConstructor
    public static class CreatedEvent  {
        private final Long postId;
        private final String parentCommentOrder;
    }
}
