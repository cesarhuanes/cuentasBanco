package com.report.report_ms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class AccountResponse implements Serializable {

    private String id;
    private String client;
    private String typeClient;
    private double saldo;
    private String typeAccount;
}
