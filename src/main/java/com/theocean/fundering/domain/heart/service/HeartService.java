package com.theocean.fundering.domain.heart.service;

import com.theocean.fundering.domain.heart.repository.HeartRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;

    public void addHeart(Long memberId, Long postId){
        final Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception400("")
        );
        try{
            heartRepository.saveHeart(memberId, post.getPostId());
            post.addHeartCount();
        } catch (final RuntimeException e) {
            throw new Exception500("");
        }
    }

    public void unHeart(Long memberId, Long postId){
        final Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception400("")
        );
        try{
            heartRepository.saveUnHeart(memberId, post.getPostId());
            post.minusHeartCount();
        } catch (final RuntimeException e) {
            throw new Exception500("");
        }
    }
}
