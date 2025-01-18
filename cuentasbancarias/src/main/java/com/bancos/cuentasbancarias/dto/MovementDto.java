package com.bancos.cuentasbancarias.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MovementDto {
    private double amount;
    private LocalDateTime dateMovement;
    private String typeMovement;
}
