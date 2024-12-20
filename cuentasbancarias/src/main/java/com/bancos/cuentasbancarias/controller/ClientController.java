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
    private ClientService clienteService;

    @GetMapping()
    public Mono<ResponseEntity<Flux<Client>>>  listarClientes(){
     return Mono.just(ResponseEntity.ok()
             .contentType(MediaType.APPLICATION_JSON_UTF8)
             .body(clienteService.findAll()) );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Client>>  getCliente(@PathVariable String id){

        return clienteService.findById(id)
                .map(c->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> guardarCliente(@Valid @RequestBody Mono<Client> monoCliente) {

        Map<String, Object> respuesta = new HashMap<>();

        return monoCliente.flatMap(cliente -> {
            return clienteService.save(cliente).map(c -> {
                respuesta.put("cliente", c);
                respuesta.put("mensaje", "Cliente Guardado con Exito");
                respuesta.put("timestamp", new Date());

                return ResponseEntity.created(URI.create("/api/clientes/".concat(String.valueOf(c.getId()))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(respuesta);

            });

        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo:" + fieldError.getField() + "" + fieldError.getDefaultMessage())
                    .collectList().flatMap(list -> {
                        respuesta.put("cliente", list);
                        respuesta.put("timestamp", new Date());
                        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });

        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Client>> editarCliente(@RequestBody Client client, @PathVariable String id) {
        return clienteService.findById(id).flatMap(c -> {
                    c.setNombre(client.getNombre());
                    c.setClientType(client.getClientType());
                    return clienteService.save(c);
                }).map(c -> ResponseEntity.created(URI.create("/api/clientes/".concat(String.valueOf(c.getId()))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarCliente(@PathVariable String id) {
        return clienteService.findById(id).flatMap(c -> {
            return clienteService.delete(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/{clienteId}/agregarCuentas")
    public Mono<List<Account>> agregarCuentasCliente(@PathVariable String clienteId, @RequestBody List<Account> lstAccounts) {
        return clienteService.saveCuentaByCliente(clienteId, lstAccounts);
    }
}
