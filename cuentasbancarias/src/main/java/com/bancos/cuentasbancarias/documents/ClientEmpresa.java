package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "tipoCliente")
public class ClientEmpresa extends ClientType {
  private List<CuentaAhorro> cuentaAhorroList;
  private List<CuentaCorriente> cuentaCorrienteList;
}
