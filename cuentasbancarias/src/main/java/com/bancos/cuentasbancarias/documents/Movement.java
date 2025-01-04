package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "movimiento")
public class Movement {
    @Id
    ObjectId id;
    ObjectId productoId;// Puede ser una cuenta bancaria o crédito
    TypeProduct typeProduct;// Tipo de producto: "ACCOUNT" o "CREDIT"
    double amount;
    LocalDateTime  dateMovement;
    TypeMovement typeMovement;//deposito o retiro
    Account account;
    Credit credit;

}
