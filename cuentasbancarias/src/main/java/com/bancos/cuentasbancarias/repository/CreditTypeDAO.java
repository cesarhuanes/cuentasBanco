package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditTypeDAO extends ReactiveMongoRepository<CreditType, ObjectId> {
}
