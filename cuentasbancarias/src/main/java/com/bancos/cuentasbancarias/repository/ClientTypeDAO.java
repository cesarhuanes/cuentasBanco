package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.ClientType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientTypeDAO extends ReactiveMongoRepository<ClientType, ObjectId> {
}
