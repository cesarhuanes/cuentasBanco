package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Person;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonDAO extends ReactiveMongoRepository<Person, ObjectId> {
}
