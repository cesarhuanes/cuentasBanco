package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.ClientType;
import reactor.core.publisher.Mono;

public interface ClienteTypeService {
    Mono<ClientType> save(ClientType clienteType);
}
