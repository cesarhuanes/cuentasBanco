package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Cliente;
import com.bancos.cuentasbancarias.documents.Cuenta;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CuentaDAO  extends ReactiveMongoRepository<Cuenta, ObjectId> {
}
