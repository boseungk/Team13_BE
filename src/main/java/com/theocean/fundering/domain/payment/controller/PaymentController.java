package com.theocean.fundering.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.service.PaymentService;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/posts/{postId}/verify")
    public IamportResponse<Payment> verifyByImpUid(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                   @RequestPart(value = "dto") final PaymentRequest.DonateDTO donateDTO,
                                                   @RequestParam("imp_uid") final String impUid,
                                                   @PathVariable("postId") Long postId) {
        try {
            final String email = userDetails.getEmail();
            return paymentService.verifyByImpUid(email, donateDTO, impUid, postId);
        }
        catch (final IamportResponseException | IOException e){
            throw new Exception400("결제 검증 실패");
        }
    }

}
