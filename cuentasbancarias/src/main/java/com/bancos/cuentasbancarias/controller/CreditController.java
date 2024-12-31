package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.CreditTypeService;
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

    @PostMapping
    public Mono<Credit> createCredit(@RequestBody Credit credit) {
        return creditService.saveCredit(credit);
    }
}
