package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document (collection = "tipoCuenta")
public class CuentaAhorro extends AccountType{//cuenta de ahorros

    private int limitMount;

}
