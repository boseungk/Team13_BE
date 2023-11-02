package com.theocean.fundering.domain.payment.service;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.repository.PaymentRepository;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.config.PaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentConfig paymentConfig;
    private final PaymentRepository paymentRepository;
    private final PostRepository postRepository;

    public IamportResponse<Payment> verifyByImpUid(String impUid) throws IamportResponseException, IOException {
        return paymentConfig.iamportClient().paymentByImpUid(impUid);
    }

    @Transactional
    public void donate(Long postId, Member member, String impUid, PaymentRequest.DonateDTO donateDTO){
        Post post = postRepository.findById(postId).orElseThrow();
        paymentRepository.save(donateDTO.toEntity(member, post, impUid));
    }


}
