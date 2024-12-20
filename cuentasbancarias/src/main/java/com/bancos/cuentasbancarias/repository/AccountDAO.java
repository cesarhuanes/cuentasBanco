package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountDAO extends ReactiveMongoRepository<Account, ObjectId> {
}
