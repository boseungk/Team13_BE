package com.theocean.fundering.domain.post.service;

import com.theocean.fundering.domain.member.domain.Admin;
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
    private AdminRepository adminRepository;
    private PaymentRepository paymentRepository;

    @PostUpdate
    public void addAdminEvent(final Post post){
        if (PostStatus.COMPLETE == post.getPostStatus()){
            var supporterList = paymentRepository.findAllSupporterByPostId(post.getPostId());
            for(int i = 0; i < 3; i++) {
                adminRepository.save(Admin.builder()
                        .memberId(supporterList.get(i))
                        .postId(post.getPostId())
                        .build());
            }
        }
    }
}