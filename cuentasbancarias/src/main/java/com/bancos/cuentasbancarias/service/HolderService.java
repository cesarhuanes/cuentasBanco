package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.documents.Holder;
import reactor.core.publisher.Mono;

public interface HolderService {
    Mono<Holder> saveHolder(Holder holder);
}
