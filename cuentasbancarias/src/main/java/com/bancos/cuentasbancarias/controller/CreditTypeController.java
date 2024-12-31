package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.service.AccountTypeService;
import com.bancos.cuentasbancarias.service.CreditTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/creditType")
public class CreditTypeController {
    private final CreditTypeService creditTypeService;

    @PostMapping
    public Mono<CreditType> createCreditType(@RequestBody CreditType creditType) {
        return creditTypeService.saveCreditType(creditType);
    }
}
