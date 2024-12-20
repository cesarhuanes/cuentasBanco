package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    public Mono<Account> createCuenta(Account account);
    public Mono<Account> getCuentaById(String id);
    public Flux<Account> getAllCuentas();
    public Mono<Account> updateCuenta(String id, Account account);
    public Mono<Void> deleteCuenta(Account account);

}
