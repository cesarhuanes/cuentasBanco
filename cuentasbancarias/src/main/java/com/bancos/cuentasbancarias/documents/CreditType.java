package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tipoCredito")
public class CreditType {
    @Id
    private ObjectId id;
    private String nombreCredito;
}
