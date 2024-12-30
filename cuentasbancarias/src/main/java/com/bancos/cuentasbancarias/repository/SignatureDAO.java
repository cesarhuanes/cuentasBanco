package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Signature;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SignatureDAO extends ReactiveMongoRepository<Signature, ObjectId> {
}
