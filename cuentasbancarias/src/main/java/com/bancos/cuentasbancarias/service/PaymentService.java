package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.Payment;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Payment> savePayment(Payment payment);
    Mono<Credit> makePayment(ObjectId creditId, double amount, ObjectId payerId);
}
