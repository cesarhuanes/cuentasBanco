package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Holder;
import com.bancos.cuentasbancarias.documents.Signature;
import reactor.core.publisher.Mono;

public interface SignatureService {
    Mono<Signature> saveSignature(Signature signature);
}
