package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AccountDAO extends ReactiveMongoRepository<Account, ObjectId> {
    Flux<Account> findByClientId(ObjectId clientId);
}
