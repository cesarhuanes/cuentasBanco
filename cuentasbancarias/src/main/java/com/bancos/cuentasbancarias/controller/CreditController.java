package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.service.CreditService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/credit")
public class CreditController {
    private final CreditService creditService;
    /**R1 Funcionalidades 7**/
    @PostMapping
    public Mono<Credit> createCredit(@RequestBody Credit credit) {
        return creditService.saveCredit(credit);
    }
}
