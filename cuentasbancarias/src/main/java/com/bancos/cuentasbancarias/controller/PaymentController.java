package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.Payment;

import com.bancos.cuentasbancarias.service.PaymentService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
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
    /**R3 4.Un cliente puede hacer el pago de cualquier producto de
     * crédito de terceros.**/
    @PostMapping("/{creditId}/credit")
    public Mono<Credit> makePayment(@PathVariable ObjectId creditId,
                                         @RequestParam double amount,
                                         @RequestParam ObjectId payerId) {
        return paymentService.makePayment(creditId, amount, payerId);
    }
}
