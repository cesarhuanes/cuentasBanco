package com.cuentabancaria.cuenta_bancaria_fallback.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class AccountResponse {
    private String id;
    private String client;
    private String typeClient;
    private double saldo;
    private String typeAccount;

}
