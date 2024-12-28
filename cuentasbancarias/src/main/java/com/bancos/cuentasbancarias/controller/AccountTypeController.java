package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.service.AccountTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accountType")
public class AccountTypeController {
    private final AccountTypeService accountTypeService;

    @PostMapping
    public Mono<AccountType> createAccountType(@RequestBody AccountType accountType) {
        return accountTypeService.save(accountType);
    }
}
