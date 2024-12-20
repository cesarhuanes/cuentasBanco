package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientDAO extends ReactiveMongoRepository<Client, ObjectId> {
}
