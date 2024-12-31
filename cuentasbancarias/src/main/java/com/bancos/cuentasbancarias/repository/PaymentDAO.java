package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.Payment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentDAO extends ReactiveMongoRepository<Payment, ObjectId> {

}
