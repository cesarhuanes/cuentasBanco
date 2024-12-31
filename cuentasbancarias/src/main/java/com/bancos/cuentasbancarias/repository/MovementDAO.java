package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Movement;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovementDAO extends ReactiveMongoRepository<Movement, ObjectId> {
}
