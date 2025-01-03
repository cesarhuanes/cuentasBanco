package com.bancos.cuentasbancarias.service;

import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface DebtCheckService {
    public Mono<Boolean> hasOverdueDebt(ObjectId clientId);
}
