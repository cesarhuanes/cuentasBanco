package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClientService {
    Flux<Client> findAll();
    Mono<Client> findById(String id);
    Mono<Client> save(Client client);
    Mono<Void>   deleteById(String id);
    Mono<List<Account>> saveCuentaByCliente(String clienteId, List<Account> lstAccounts);
}
