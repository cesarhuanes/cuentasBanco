package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "titular")
public class Holder extends Person{//titular
}
