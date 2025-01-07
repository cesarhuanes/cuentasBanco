package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "tipoCuenta")
public class PlazoFijo extends AccountType{//cuenta a plazo fijo
private Date dateMovement;
}
