package com.bancos.cuentasbancarias.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountResponse {
    private String id;
    private String client;
    private String typeClient;
    private double saldo;
    private String typeAccount;

}
