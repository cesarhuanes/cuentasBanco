package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Cliente;
import com.bancos.cuentasbancarias.documents.Cuenta;
import com.bancos.cuentasbancarias.repository.CuentaDAO;
import com.bancos.cuentasbancarias.service.CuentaService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Slf4j
@Service
public class CuentaServiceImpl implements CuentaService {
    @Autowired
    private CuentaDAO cuentaDAO;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Cuenta> createCuenta(Cuenta cuenta) {
        return cuentaDAO.save(cuenta);
    }

    @Override
    public Mono<Cuenta> getCuentaById(String id) {
        ObjectId objectId = new ObjectId(id);
        return cuentaDAO.findById(objectId);

    }

    @Override
    public Flux<Cuenta> getAllCuentas() {
        log.info("Listado de cuentas",cuentaDAO.findAll());
        return cuentaDAO.findAll();
    }

    @Override
    public Mono<Cuenta> updateCuenta(String id, Cuenta cuenta) {
        ObjectId objectId=new ObjectId(id);
        return cuentaDAO.findById(objectId)
                .flatMap(existeCuenta->{
                   // existeCuenta.setId(cuenta.getId());
                    existeCuenta.setSaldo(cuenta.getSaldo());
                    existeCuenta.setAccountType(cuenta.getAccountType());
                    existeCuenta.setCliente_id(cuenta.getCliente_id());
                    return cuentaDAO.save(existeCuenta);
                });
    }

    @Override
    public Mono<Void> deleteCuenta(Cuenta cuenta) {
        return cuentaDAO.delete(cuenta);
    }
}
