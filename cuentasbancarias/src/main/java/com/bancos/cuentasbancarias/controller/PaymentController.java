package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Payment;

import com.bancos.cuentasbancarias.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/pay")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Mono<Payment> createPay(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }
}
