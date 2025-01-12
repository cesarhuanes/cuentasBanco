package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/creditCard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @GetMapping("/{creditCardId}/balance")
    public Mono<Double> getCreditCardBalance(@PathVariable String creditCardId) {
        return creditCardService.getCreditCardBalance(creditCardId);
    }
}
