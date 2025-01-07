package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "firmante")
public class Signature extends Person{//firmante
}
