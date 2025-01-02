package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tarjetaCredito")
public class CreditCard {
    @Id
    ObjectId id;
    double limitCredit;
    double amountAviable;

    ObjectId clientId;
    Client client;


}
