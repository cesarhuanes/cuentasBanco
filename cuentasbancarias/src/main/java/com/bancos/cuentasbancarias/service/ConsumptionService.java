package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Consumption;
import com.bancos.cuentasbancarias.util.Constants;
import reactor.core.publisher.Mono;

public interface ConsumptionService {
   Mono<Consumption> saveConsumption(Consumption consumption);

}
