package com.bancos.cuentasbancarias.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
  private String productId;
  private String productName;
  private List<MovementDto> lstMovement;

}
