package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "cuenta")
public class Account {
    @Id
    private ObjectId id;
    private double saldo;

    private ObjectId accountTypeId;
    private AccountType accountType;

    private ObjectId clienteId;
    private Client client;

    private List<Holder> titulares;
    private List<Signature> firmantesAutorizados;
    private List<Credit> creditList;

}
