package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = false)
@Data
@Document(collection = "titular")
public class Holder extends Person{//titular
}
