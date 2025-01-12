package com.cuentabancaria.cuenta_bancaria_fallback.controller;

import com.cuentabancaria.cuenta_bancaria_fallback.dto.AccountResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {
  private static final AccountResponse  ACCOUNT_RESPONSE=
          AccountResponse.builder()
                  .id("6771c8ffdf0619255b240e8e")
                  .client("CLIENT_DEFAULT")
                  .typeAccount("ACCOUNT_DEFAULT")
                  .saldo(1000)
                  .typeClient("TYPE_CLIENT-DEFAULT")
                  .build();

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponse>>  getCuenta(@PathVariable String id){

        return Mono.just(ACCOUNT_RESPONSE)
                .map(c->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
