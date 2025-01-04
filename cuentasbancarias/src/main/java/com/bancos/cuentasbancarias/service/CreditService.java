package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Credit;

import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<Credit> saveCredit(Credit credit);
    public Mono<Credit> updateCredit(String id, Credit credit);
}
