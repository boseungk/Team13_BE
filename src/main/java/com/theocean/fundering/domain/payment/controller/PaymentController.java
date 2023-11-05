package com.theocean.fundering.domain.payment.controller;


import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.theocean.fundering.domain.payment.dto.PaymentRequest;
import com.theocean.fundering.domain.payment.service.PaymentService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/posts/{postId}/verify")
    public IamportResponse<Payment> verifyByImpUid(@PathVariable final Long postId,
                                                   @RequestParam("imp_uid") final String impUid) throws IamportResponseException, IOException {
        return paymentService.verifyByImpUid(impUid);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/posts/{postId}/donate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> donate(@PathVariable final Long postId,
                               @AuthenticationPrincipal final CustomUserDetails userDetails,
                               @RequestBody final PaymentRequest.DonateDTO donateDTO) {
        final String email = userDetails.getEmail();
        paymentService.donate(postId, email, donateDTO);
        return ApiResult.success(null);
    }


}
