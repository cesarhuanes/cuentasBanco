package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "cuenta")
public class Cuenta {
    @Id
    private ObjectId id;
    private double saldo;
    private AccountType accountType;

    @Field("cliente_id")
    private ObjectId cliente_id;
    @Transient
    private Cliente cliente;
}
