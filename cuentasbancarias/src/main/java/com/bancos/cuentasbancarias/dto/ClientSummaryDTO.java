package com.bancos.cuentasbancarias.dto;

import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditCard;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientSummaryDTO {
    private List<Account> accounts;
    private List<CreditCard> creditCards;
    private List<Credit> credits;
}
