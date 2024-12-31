package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "credito")
public class Credit {
    @Id
    private ObjectId id;

    private double amount;//monto
    private double amountAviable;//saldo disponible

    private ObjectId creditTypeId;
    private CreditType creditType;

    private ObjectId clientId;
    private Client client;
}
