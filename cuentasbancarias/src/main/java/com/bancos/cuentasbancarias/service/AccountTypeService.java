package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Client;
import reactor.core.publisher.Mono;

public interface AccountTypeService {
    Mono<AccountType> save(AccountType accountType);
}
