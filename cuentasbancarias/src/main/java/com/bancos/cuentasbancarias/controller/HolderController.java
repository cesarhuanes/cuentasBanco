package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.documents.Holder;
import com.bancos.cuentasbancarias.service.HolderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/holder")
public class HolderController {
  private final HolderService holderService;
    @PostMapping
    public Mono<Holder> createHolder(@RequestBody Holder holder) {
        return holderService.saveHolder(holder);
    }

}
