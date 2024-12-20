package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Cliente;
import com.bancos.cuentasbancarias.documents.Cuenta;
import com.bancos.cuentasbancarias.service.CuentaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping()
    public Mono<ResponseEntity<List<Cuenta>>> getAllAccounts() {
        log.info("CuentaController.getAllAccounts");
        return cuentaService.getAllCuentas()
                .collectList()
                .map(ResponseEntity::ok);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Cuenta>>  getCuenta(@PathVariable String id){

        return cuentaService.getCuentaById(id)
                .map(c->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> guardarCuenta(@Valid @RequestBody Mono<Cuenta> monoCuenta) {

        Map<String, Object> respuesta = new HashMap<>();

        return monoCuenta.flatMap(cuenta -> {
            return cuentaService.createCuenta(cuenta).map(c -> {
                respuesta.put("cuenta", c);
                respuesta.put("mensaje", "Cuenta Guardado con Exito");
                respuesta.put("timestamp", new Date());

                return ResponseEntity.created(URI.create("/api/cuenta/".concat(String.valueOf(c.getId()))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(respuesta);

            });

        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo:" + fieldError.getField() + "" + fieldError.getDefaultMessage())
                    .collectList().flatMap(list -> {
                        respuesta.put("cuenta", list);
                        respuesta.put("timestamp", new Date());
                        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });

        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cuenta>> editarCuenta(@RequestBody Cuenta cuenta, @PathVariable String id) {
        return cuentaService.updateCuenta(id,cuenta)
                .map(c -> ResponseEntity.created(URI.create("/api/cuenta/".concat(String.valueOf(c.getId()))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarCliente(@PathVariable String id) {
        return cuentaService.getCuentaById(id).flatMap(c -> {
            return cuentaService.deleteCuenta(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

    }





}
