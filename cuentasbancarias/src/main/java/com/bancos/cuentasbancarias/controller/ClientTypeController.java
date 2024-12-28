package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.service.ClienteTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/clientType")
public class ClientTypeController {

    private final ClienteTypeService clienteTypeService;

    @PostMapping
    public Mono<ClientType> createClienteType(@RequestBody ClientType clienteType) {
        return clienteTypeService.save(clienteType);
    }
}
