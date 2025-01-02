package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditCardDAO extends ReactiveMongoRepository<CreditCard,Object> {
}
