package com.theocean.fundering.domain.post.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.payment.repository.PaymentRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.domain.post.repository.PostRepository;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostEventListener{
    private MemberRepository memberRepository;
    private PostRepository postRepository;
    private AdminRepository adminRepository;
    private PaymentRepository paymentRepository;

    @PostUpdate
    public void event(final Post post){
        List<Long> paymentRank = new LinkedList<>();
        if (PostStatus.COMPLETE == post.getPostStatus()){
            var supporterList = paymentRepository.findAllSupporterByPostId(post.getPostId());
            for (Member m : supporterList){

            }
        }
    }
}