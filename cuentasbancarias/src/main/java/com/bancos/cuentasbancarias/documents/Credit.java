package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "credito")
public class Credit {
    @Id
    private ObjectId id;

    private double amount;//monto
    private double amountAvailable;//saldo disponible
    private Date dueDate; // Fecha de vencimiento
    private boolean isOverdue; // Indica si la deuda está vencida

    private ObjectId creditTypeId;
    private CreditType creditType;

    private ObjectId clientId;
    private Client client;
    // Método para actualizar el estado de la deuda
    public void updateOverdueStatus() {
        this.isOverdue = new Date().after(this.dueDate);
    }
}
