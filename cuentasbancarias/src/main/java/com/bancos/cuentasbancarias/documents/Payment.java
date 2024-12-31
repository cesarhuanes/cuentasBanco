package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "pago")
public class Payment {
    @Id
    ObjectId id;
    Date datePay;
    double amount;//monto de pago
    ObjectId creditId;
    Credit credit;
}
