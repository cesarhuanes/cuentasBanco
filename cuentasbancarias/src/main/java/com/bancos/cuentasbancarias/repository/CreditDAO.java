package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Credit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditDAO extends ReactiveMongoRepository<Credit, ObjectId> {

}
