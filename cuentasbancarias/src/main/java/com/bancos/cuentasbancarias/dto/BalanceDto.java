package com.bancos.cuentasbancarias.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class BalanceDto {
    private String accountId;
    private String typeAccount;
    private double balance;//saldo
}
