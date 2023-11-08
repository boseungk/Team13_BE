package com.theocean.fundering.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.service.PaymentService;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/donate")
    public IamportResponse<Payment> verifyByImpUidAndDonate(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                   @RequestPart(value = "dto") final PaymentRequest.DonateDTO donateDTO,
                                                   @PathVariable("postId") Long postId) {
        try {
            final String email = userDetails.getEmail();
            return paymentService.verifyByImpUidAndDonate(email, donateDTO, postId);
        }
        catch (final IamportResponseException | IOException e){
            throw new Exception400("결제 검증 실패");
        }
    }

}
