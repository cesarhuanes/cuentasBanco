package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.documents.Holder;
import reactor.core.publisher.Mono;

public interface CreditTypeService {
    Mono<CreditType> saveCreditType(CreditType creditType);
}
