package com.bancos.cuentasbancarias.controller;


import com.bancos.cuentasbancarias.documents.Movement;
import com.bancos.cuentasbancarias.service.MovementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/movement")
public class MovementController {
   /**R1-Funcionalidad 8 **/
    private final MovementService movementService;
    @PostMapping
    public Mono<Movement> createMovement(@RequestBody Movement movement) {
        return movementService.saveMovement(movement);
    }

}
