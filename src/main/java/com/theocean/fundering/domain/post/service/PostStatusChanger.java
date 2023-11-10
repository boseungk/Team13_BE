package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class PostStatusChanger {
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void changePostStatus(){
        var postList = postRepository.findAll();
        postList.stream()
                .filter(p -> (p.getDeadline().toEpochSecond(ZoneOffset.UTC) <= LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) && (p.getAccount().getBalance() >= p.getTargetPrice()))
                .forEach(p -> p.changeStatus(PostStatus.COMPLETE));
        postList.stream()
                .filter(p -> (p.getDeadline().toEpochSecond(ZoneOffset.UTC) <= LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) && (p.getAccount().getBalance() < p.getTargetPrice()))
                .forEach(p -> p.changeStatus(PostStatus.CLOSED));
    }
}
