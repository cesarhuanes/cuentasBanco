package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document (collection = "tipoCuenta")
public class AccountType {
    @Id
    private ObjectId id;
    private String nombre;
}
