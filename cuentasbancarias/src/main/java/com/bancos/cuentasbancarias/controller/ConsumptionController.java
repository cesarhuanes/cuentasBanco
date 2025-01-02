package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.documents.Consumption;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.service.ConsumptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @PostMapping
    public Mono<Consumption> createCreditCard(@RequestBody Consumption consumption){
        return consumptionService.saveConsumption(consumption);
    }

}
