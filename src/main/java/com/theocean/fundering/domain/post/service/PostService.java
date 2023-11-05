package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AWSS3Uploader awss3Uploader;
    private final CelebRepository celebRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void writePost(final String email, final PostRequest.PostWriteDTO dto, final MultipartFile thumbnail) {
        dto.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        final Member writer = memberRepository.findByEmail(email).orElseThrow(
                () -> new Exception500("No matched member found")
        );
        final Celebrity celebrity = celebRepository.findById(dto.getCelebId()).orElseThrow(
                () -> new Exception500("No matched celebrity found")
        );
        postRepository.save(dto.toEntity(writer, celebrity));
    }

    public PostResponse.FindByPostIdDTO findByPostId(final String email, final Long postId) {
        final Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        final PostResponse.FindByPostIdDTO result = new PostResponse.FindByPostIdDTO(postPS);
        if (postPS.getWriter().getEmail().equals(email))
            result.setEqWriter(true);
        return result;
    }

    public PageResponse<PostResponse.FindAllDTO> findAll(@Nullable final Long postId, final Pageable pageable) {
        final var postList = postRepository.findAll(postId, pageable);
        return new PageResponse<>(postList);

    }

    public PageResponse<PostResponse.FindAllDTO> findAllByWriterId(@Nullable final Long postId, final String email, final Pageable pageable) {
        final var postList = postRepository.findAllByWriterEmail(postId, email, pageable);
        return new PageResponse<>(postList);
    }

    @Transactional
    public Long editPost(final Long postId, final PostRequest.PostEditDTO dto, @Nullable final MultipartFile thumbnail) {
        if (null != thumbnail)
            dto.setThumbnail(awss3Uploader.uploadToS3(thumbnail));
        final Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        postPS.update(dto.getTitle(), dto.getIntroduction(), dto.getThumbnail(), dto.getTargetPrice(), dto.getDeadline(), dto.getModifiedAt());
        return postId;
    }

    public void deletePost(final Long postId) {
        postRepository.deleteById(postId);
    }

    public PageResponse<PostResponse.FindAllDTO> searchPost(@Nullable final Long postId, final String keyword, final Pageable pageable) {
        final var postList = postRepository.findAllByKeyword(postId, keyword, pageable);
        return new PageResponse<>(postList);

    }


    public String getIntroduction(final Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No mathced post found")
        ).getIntroduction();
    }

}
