package com.bancos.cuentasbancarias.repository;

import com.bancos.cuentasbancarias.dto.MovementDTO;
import com.bancos.cuentasbancarias.documents.Movement;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovementDAO extends ReactiveMongoRepository<Movement, ObjectId> {
    Flux<MovementDTO> findByAccountId(ObjectId accountId);

}
