package com.bancos.cuentasbancarias.documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credito")
public class Credit {
    @Id
    private ObjectId id;
    private String clientId;
    private double amount;
    private double limit;
}
