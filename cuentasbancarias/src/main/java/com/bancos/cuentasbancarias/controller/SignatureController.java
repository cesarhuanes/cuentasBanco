package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.documents.Signature;
import com.bancos.cuentasbancarias.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/signature")
public class SignatureController {
    private final SignatureService signatureService;
    @PostMapping
    public Mono<Signature> createSignature(@RequestBody Signature signature) {
        return signatureService.saveSignature(signature);
    }

}
