package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.PaymentRequest;
import com.bancos.cuentasbancarias.service.PaymentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RequestMapping(path = "/api/pay")
@RestController
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    /**R1 -9  Y R3 -4.Un cliente puede hacer el pago de cualquier producto de
     * crédito de terceros.**/
    @PostMapping
    public Mono<ResponseEntity<Credit>> paycredit(@RequestBody PaymentRequest paymentRequest) {
        logger.info("POST PaymentController.paycredit={}");
        return paymentService.makePayment(paymentRequest.getCreditId(),
                paymentRequest.getAmount(), paymentRequest.getPayerId())
          .map(saveCredit -> ResponseEntity.status(200).body(saveCredit))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
