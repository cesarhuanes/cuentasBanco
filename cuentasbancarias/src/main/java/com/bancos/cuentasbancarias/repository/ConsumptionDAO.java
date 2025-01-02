package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Consumption;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ConsumptionDAO extends ReactiveMongoRepository<Consumption, ObjectId> {
}
