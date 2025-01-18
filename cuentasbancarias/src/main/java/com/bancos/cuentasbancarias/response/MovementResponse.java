package com.bancos.cuentasbancarias.response;

import com.bancos.cuentasbancarias.dto.ProductDto;
import lombok.Data;

import java.util.List;

@Data
public class MovementResponse {
  private String clientName;
  private List<ProductDto> lstProduct;

}
