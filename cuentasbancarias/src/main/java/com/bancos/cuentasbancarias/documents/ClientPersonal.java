package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tipoCliente")
public class ClientPersonal extends ClientType {
    private CuentaAhorro cuentaAhorro;
    private CuentaCorriente cuentaCorriente;
    private PlazoFijo plazoFijo;
}
