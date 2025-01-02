package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.CreditCard;
import reactor.core.publisher.Mono;

public interface CreditCardService {

    Mono<CreditCard> saveCreditCard(CreditCard creditCard);
    Mono<Double> getCreditCardBalance(String creditCardId) ;

    }
