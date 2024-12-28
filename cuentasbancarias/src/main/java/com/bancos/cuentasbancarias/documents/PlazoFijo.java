package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Document(collection = "tipoCuenta")
public class PlazoFijo extends AccountType{//cuenta a plazo fijo
private Date dateMovement;
}
