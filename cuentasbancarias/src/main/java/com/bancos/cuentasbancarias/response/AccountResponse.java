package com.bancos.cuentasbancarias.response;

import lombok.Data;

@Data
public class AccountResponse {
    private String id;
    private String client;
    private String typeClient;
    private double saldo;
    private String typeAccount;

}
