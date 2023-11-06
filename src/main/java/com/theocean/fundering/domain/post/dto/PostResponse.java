package com.theocean.fundering.domain.post.dto;

import com.theocean.fundering.domain.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostResponse {


    @Getter
    @Setter
    public static class FindByPostIdDTO { // 게시글 열람 DTO
        private Long postId;
        private String writer;
        private String writerImg;
        private String celebrity;
        private String celebImg;
        private String title;
        private String content;
        private String thumbnail;
        private int targetPrice;
        private int currentAmount;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private int participant;
        private boolean eqWriter;

        public FindByPostIdDTO(final Post post) {
            postId = post.getPostId();
            writer = post.getWriter().getNickname();
            writerImg = post.getWriter().getProfileImage();
            celebrity = post.getCelebrity().getCelebName();
            celebImg = post.getCelebrity().getProfileImage();
            title = post.getTitle();
            content = post.getIntroduction();
            thumbnail = post.getThumbnail();
            targetPrice = post.getTargetPrice();
            currentAmount = post.getAccount().getBalance();
            deadline = post.getDeadline();
            createdAt = post.getCreatedAt();
            modifiedAt = post.getModifiedAt();
            participant = post.getParticipants();
            eqWriter = false;
        }
    }

    @Getter
    @Setter
    public static class FindAllDTO {
        private Long postId;
        private String writer;
        private String celebrity;
        private String celebImg;
        private String title;
        private String thumbnail;
        private int targetPrice;
        private int currentAmount;
        private LocalDateTime deadline;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public FindAllDTO(final Post post) {
            postId = post.getPostId();
            writer = post.getWriter().getNickname();
            celebrity = post.getCelebrity().getCelebName();
            celebImg = post.getCelebrity().getProfileImage();
            title = post.getTitle();
            thumbnail = post.getThumbnail();
            targetPrice = post.getTargetPrice();
            currentAmount = post.getAccount().getBalance();
            deadline = post.getDeadline();
            createdAt = post.getCreatedAt();
            modifiedAt = post.getModifiedAt();
        }
    }

}
