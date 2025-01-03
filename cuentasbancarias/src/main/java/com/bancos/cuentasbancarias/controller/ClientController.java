package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.dto.ClientSummaryDTO;
import com.bancos.cuentasbancarias.service.ClientService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;


@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public Flux<Client> getAllClients() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Client>> getClientById(@PathVariable String id) {
        return clientService.findById(id)
                .map(client -> ResponseEntity.ok(client))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Client>> createClient(@RequestBody Client client) {
        return clientService.save(client)
                .map(savedClient -> ResponseEntity.status(201).body(savedClient))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Client>> updateClient(@PathVariable String id, @RequestBody Client client) {
        return clientService.findById(id)
                .flatMap(existingClient -> {
                    existingClient.setNombre(client.getNombre());
                    existingClient.setClientTypeId(client.getClientTypeId());
                    return clientService.save(existingClient);
                })
                .map(updatedClient -> ResponseEntity.ok(updatedClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable String id) {
        return clientService.deleteById(id)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/{clienteId}/agregarCuentas")
    public Mono<ResponseEntity<List<Account>>> agregarCuentasCliente(@PathVariable String clienteId, @RequestBody List<Account> lstAccounts) {
        return clientService.saveCuentaByCliente(clienteId, lstAccounts)
                .map(savedccounts -> ResponseEntity.status(HttpStatus.OK).body(savedccounts))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }
    /***1.	Permitir elaborar un resumen consolidado de un cliente con
     * todos los productos que pueda tener en el banco.*/
    @GetMapping("/{clientId}/summary")
    public Mono<ClientSummaryDTO> getClientSummary(@PathVariable ObjectId clientId) {
        return clientService.getClientSummary(clientId);
    }
}
