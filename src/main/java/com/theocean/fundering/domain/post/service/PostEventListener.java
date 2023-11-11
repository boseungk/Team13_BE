package com.theocean.fundering.domain.post.service;

import com.theocean.fundering.domain.member.domain.Admin;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.payment.repository.PaymentRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {
    private static final int PAYMENT_TOP_3 = 3;
    private AdminRepository adminRepository;
    private PaymentRepository paymentRepository;

    @PostUpdate
    public void addAdminEvent(final Post post) {
        if (PostStatus.COMPLETE == post.getPostStatus()) {
            final var supporterList = paymentRepository.findAllSupporterByPostId(post.getPostId());
            for (int i = 0; PAYMENT_TOP_3 > i; i++) {
                adminRepository.save(Admin.builder()
                        .memberId(supporterList.get(i))
                        .postId(post.getPostId())
                        .build());
            }
        }
    }
}