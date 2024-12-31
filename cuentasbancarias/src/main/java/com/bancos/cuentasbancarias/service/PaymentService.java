package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Payment;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Payment> savePayment(Payment payment);
}
