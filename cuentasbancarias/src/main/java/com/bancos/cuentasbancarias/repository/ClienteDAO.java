package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Cliente;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClienteDAO extends ReactiveMongoRepository<Cliente, ObjectId> {
}
