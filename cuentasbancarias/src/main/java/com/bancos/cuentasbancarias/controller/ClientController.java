package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
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
    public Mono<List<Account>> agregarCuentasCliente(@PathVariable String clienteId, @RequestBody List<Account> lstAccounts) {
        return clientService.saveCuentaByCliente(clienteId, lstAccounts);
    }
}
