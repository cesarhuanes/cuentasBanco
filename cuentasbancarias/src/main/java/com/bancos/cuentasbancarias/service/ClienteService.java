package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Cliente;
import com.bancos.cuentasbancarias.documents.Cuenta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClienteService {
    Flux<Cliente> findAll();
    Mono<Cliente> findById(String id);
    Mono<Cliente> save(Cliente cliente);
    Mono<Void>   delete(Cliente cliente);
    Mono<List<Cuenta>> saveCuentaByCliente(String clienteId, List<Cuenta> lstCuentas);
}
