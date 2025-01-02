package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.documents.Payment;
import com.bancos.cuentasbancarias.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/creditCard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @PostMapping
    public Mono<CreditCard> createCreditCard(@RequestBody CreditCard creditCard){
        return creditCardService.saveCreditCard(creditCard);
    }
}
