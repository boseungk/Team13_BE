package com.theocean.fundering.domain.payment.domain.service;


import com.theocean.fundering.global.config.PaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentConfig paymentConfig;



}
