package com.theocean.fundering.domain.post.service;

<<<<<<< HEAD:src/main/java/com/theocean/fundering/domain/post/service/HeartService.java
import com.theocean.fundering.domain.post.repository.HeartRepository;
=======
import com.theocean.fundering.domain.heart.repository.HeartRepository;
>>>>>>> 96e9f507e12586585985b0053a2b57f58d097acb:src/main/java/com/theocean/fundering/domain/heart/service/HeartService.java
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
