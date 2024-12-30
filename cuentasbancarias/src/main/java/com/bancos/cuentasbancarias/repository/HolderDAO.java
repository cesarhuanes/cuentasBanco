package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Holder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface HolderDAO extends ReactiveMongoRepository<Holder, ObjectId> {
}
