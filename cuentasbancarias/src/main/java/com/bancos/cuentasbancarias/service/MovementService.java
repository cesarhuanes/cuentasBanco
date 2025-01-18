package com.bancos.cuentasbancarias.service;

import com.bancos.cuentasbancarias.documents.Movement;
import reactor.core.publisher.Mono;

public interface MovementService {
    Mono<Movement> saveMovement(Movement movement);
    }
