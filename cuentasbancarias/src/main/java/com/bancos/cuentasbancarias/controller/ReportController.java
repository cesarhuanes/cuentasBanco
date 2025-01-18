package com.bancos.cuentasbancarias.controller;

import com.bancos.cuentasbancarias.response.BalanceResponse;
import com.bancos.cuentasbancarias.service.ReportService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;
    /**R1-11.	El sistema debe permitir consultar los saldos disponibles en sus productos como: cuentas bancarias y tarjetas de crédito. **/
    @GetMapping("/{clientId}")
    public Mono<BalanceResponse> getAccountBalance(@PathVariable ObjectId clientId) {
        return reportService.getAccountBalance(clientId);
    }
}
