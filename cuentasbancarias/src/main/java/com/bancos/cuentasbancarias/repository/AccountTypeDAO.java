package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.AccountType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountTypeDAO extends ReactiveMongoRepository<AccountType, ObjectId> {
}
