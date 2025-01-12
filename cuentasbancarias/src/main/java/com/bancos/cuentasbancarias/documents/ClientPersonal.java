package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = false)
@Data
@Document(collection = "tipoCliente")
public class ClientPersonal extends ClientType {
    private CuentaAhorro cuentaAhorro;
    private CuentaCorriente cuentaCorriente;
    private PlazoFijo plazoFijo;
}
