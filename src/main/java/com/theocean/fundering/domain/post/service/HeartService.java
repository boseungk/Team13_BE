package com.theocean.fundering.domain.post.service;

import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.HeartRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.ErrorCode;
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
                () -> new Exception400(ErrorCode.ER03)
        );
        heartRepository.addHeart(memberId, post.getPostId());
        post.addHeartCount();
    }

    @Transactional
    public void subtractHeart(final Long memberId, final Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception400(ErrorCode.ER03)
        );
        heartRepository.subtractHeart(memberId, post.getPostId());
        post.subtractHeartCount();
    }
}
