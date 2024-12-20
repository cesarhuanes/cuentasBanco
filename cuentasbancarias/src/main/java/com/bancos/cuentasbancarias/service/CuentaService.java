package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Cuenta;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaService {
    public Mono<Cuenta> createCuenta(Cuenta cuenta);
    public Mono<Cuenta> getCuentaById(String id);
    public Flux<Cuenta> getAllCuentas();
    public Mono<Cuenta> updateCuenta(String id, Cuenta cuenta);
    public Mono<Void> deleteCuenta(Cuenta cuenta);

}
