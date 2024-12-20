package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Account> createCuenta(Account account) {
        return accountDAO.save(account);
    }

    @Override
    public Mono<Account> getCuentaById(String id) {
        ObjectId objectId = new ObjectId(id);
        return accountDAO.findById(objectId);

    }

    @Override
    public Flux<Account> getAllCuentas() {
        log.info("Listado de cuentas", accountDAO.findAll());
        return accountDAO.findAll();
    }

    @Override
    public Mono<Account> updateCuenta(String id, Account account) {
        ObjectId objectId=new ObjectId(id);
        return accountDAO.findById(objectId)
                .flatMap(existeCuenta->{
                   // existeCuenta.setId(cuenta.getId());
                    existeCuenta.setSaldo(account.getSaldo());
                    existeCuenta.setAccountType(account.getAccountType());
                    existeCuenta.setCliente_id(account.getCliente_id());
                    return accountDAO.save(existeCuenta);
                });
    }

    @Override
    public Mono<Void> deleteCuenta(Account account) {
        return accountDAO.delete(account);
    }
}
