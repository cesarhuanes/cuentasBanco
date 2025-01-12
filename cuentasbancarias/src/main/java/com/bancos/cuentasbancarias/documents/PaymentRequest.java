package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class PaymentRequest {
    private ObjectId creditId;
    private double amount;
    private ObjectId payerId;
}
