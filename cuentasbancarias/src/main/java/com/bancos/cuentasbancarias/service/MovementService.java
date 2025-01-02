package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.MovementDTO;
import com.bancos.cuentasbancarias.documents.Movement;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementService {
    Mono<Movement> saveMovement(Movement movement);
    Flux<MovementDTO> getMovementsByAccountId(String accountId);
}
