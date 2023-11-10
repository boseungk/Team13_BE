package com.theocean.fundering.domain.heart.service;

import com.theocean.fundering.domain.heart.repository.HeartRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addHeart(final Long memberId, final Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception400("")
        );
        heartRepository.saveHeart(memberId, post.getPostId());
        post.addHeartCount();
    }

    @Transactional
    public void unHeart(final Long memberId, final Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception400("")
        );
        heartRepository.saveUnHeart(memberId, post.getPostId());
        post.minusHeartCount();
    }
}
