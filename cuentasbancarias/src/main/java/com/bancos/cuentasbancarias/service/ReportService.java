package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.response.BalanceResponse;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface ReportService {
    public Mono<BalanceResponse> getAccountBalance(ObjectId clientId);

}
