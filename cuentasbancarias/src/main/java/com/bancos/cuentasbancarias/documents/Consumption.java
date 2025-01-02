package com.bancos.cuentasbancarias.documents;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "consumo")
public class Consumption {
    @Id
    ObjectId id;

    Date dateConsumption;
    double amountConsumption;

    ObjectId creditCardId;
    CreditCard creditCard;
}
