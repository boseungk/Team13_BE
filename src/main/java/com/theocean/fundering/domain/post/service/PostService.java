package com.theocean.fundering.domain.post.service;


import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.global.dto.PageResponse;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AWSS3Uploader awss3Uploader;
    private final CelebRepository celebRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void writePost(String email, PostRequest.PostWriteDTO dto, MultipartFile thumbnail){
        String thumbnailURL = awss3Uploader.uploadToS3(thumbnail);
        Member writer =  memberRepository.findByEmail(email).orElseThrow(
                () -> new Exception500("No matched member found")
        );
        Celebrity celebrity = celebRepository.findById(dto.getCelebId()).orElseThrow(
                () -> new Exception500("No matched celebrity found")
        );
        Post newPost = postRepository.save(dto.toEntity(writer, celebrity, thumbnailURL, PostStatus.ONGOING));

        Account account = Account.builder()
                .managerId(writer.getUserId())
                .postId(newPost.getPostId())
                .build();
        accountRepository.save(account);
        newPost.registerAccount(account);
    }

    @Transactional
    public PostResponse.FindByPostIdDTO findByPostId(String email, Long postId){
        Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        PostResponse.FindByPostIdDTO result = new PostResponse.FindByPostIdDTO(postPS);
        if (postPS.getWriter().getEmail().equals(email))
            result.setEqWriter(true);
        return result;
    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> findAll(@Nullable Long postId, Pageable pageable){
        var postList = postRepository.findAll(postId, pageable);
        return new PageResponse<>(postList);

    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> findAllByWriterName(@Nullable Long postId, String nickname, Pageable pageable){
        var postList = postRepository.findAllByWriterName(postId, nickname, pageable);
        return new PageResponse<>(postList);
    }

    @Transactional
    public Long editPost(Long postId, String email, PostRequest.PostEditDTO dto, @Nullable MultipartFile thumbnail){
        String newThumbnail = null;
        Post postPS = postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No matched post found")
        );
        if (thumbnail != null)
            newThumbnail = awss3Uploader.uploadToS3(thumbnail);
        else
            newThumbnail = postPS.getThumbnail();
        if (!postPS.getWriter().getEmail().equals(email))
            throw new Exception403("");
        postPS.update(dto.getTitle(), dto.getIntroduction(), newThumbnail, dto.getTargetPrice(), dto.getDeadline(), dto.getModifiedAt());
        return postId;
    }

    @Transactional
    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    @Transactional
    public PageResponse<PostResponse.FindAllDTO> searchPostByKeyword(@Nullable Long postId, String keyword, Pageable pageable){
        var postList = postRepository.findAllByKeyword(postId, keyword, pageable);
        return new PageResponse<>(postList);

    }

    public String getIntroduction(Long postId){
        return postRepository.findById(postId).orElseThrow(
                () -> new Exception500("No mathced post found")
        ).getIntroduction();
    }

}
