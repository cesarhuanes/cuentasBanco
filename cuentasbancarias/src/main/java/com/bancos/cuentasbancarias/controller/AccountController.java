package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.dto.AccountResponse;
import com.bancos.cuentasbancarias.dto.ClientSummaryDTO;
import com.bancos.cuentasbancarias.dto.MovementDTO;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.service.AccountService;
import com.bancos.cuentasbancarias.service.MovementService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final MovementService movementService;

    @GetMapping()
    public Mono<ResponseEntity<List<Account>>> getAllAccounts() {
        return accountService.getAllCuentas()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponse>>  getCuenta(@PathVariable String id){

        return accountService.getAccountById(id)
                .map(c->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Account>> createAccount(@RequestBody Account account) {
        return accountService.createCuenta(account)
                .map(savedAccount -> ResponseEntity.status(200).body(savedAccount))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Account>> editarCuenta(@RequestBody Account account, @PathVariable String id) {
        return accountService.updateCuenta(id, account)
                .map(c -> ResponseEntity.created(URI.create("/api/cuenta/".concat(String.valueOf(c.getId()))))
                        .contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarCliente(@PathVariable String id) {
        return accountService.getCuentaById(id).flatMap(c -> {
            return accountService.deleteCuenta(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{accountId}/balance")
    public Mono<Double> getAccountBalance(@PathVariable String accountId) {
        return accountService.getAccountBalance(accountId);
    }

    @GetMapping("/{accountId}/movements")
    public Flux<MovementDTO> getMovementsByAccountId(@PathVariable String accountId) {
        return movementService.getMovementsByAccountId(accountId);
    }

}
