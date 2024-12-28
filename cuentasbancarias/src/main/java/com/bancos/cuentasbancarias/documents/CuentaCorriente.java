package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tipoCuenta")
public class CuentaCorriente extends AccountType{//cuenta corriente
    private  double commission;
}
