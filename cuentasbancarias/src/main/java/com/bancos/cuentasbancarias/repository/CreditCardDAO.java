package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.documents.CreditCard;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditCardDAO extends ReactiveMongoRepository<CreditCard,Object> {
    Flux<CreditCard> findByClientId(ObjectId clientId);
}
