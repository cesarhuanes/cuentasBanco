package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "movimiento")
public class Movement {
    @Id
    ObjectId id;
    Date dateMovement;
    double amount;
    //deposito o retiro
    TypeMovement typeMovement;
    //relacion de cuenta
    ObjectId accountId;
    Account account;

}
