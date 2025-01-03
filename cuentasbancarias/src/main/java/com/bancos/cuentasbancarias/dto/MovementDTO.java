package com.bancos.cuentasbancarias.dto;

import com.bancos.cuentasbancarias.documents.TypeMovement;
import lombok.Data;

@Data
public class MovementDTO {
    private double amount;
    private TypeMovement typeMovement;
}
