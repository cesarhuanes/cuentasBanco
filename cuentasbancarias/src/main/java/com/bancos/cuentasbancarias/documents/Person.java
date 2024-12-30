package com.bancos.cuentasbancarias.documents;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection="persona")
public class Person {
    @Id
    private ObjectId id;
    private String nombre;
    private String documentoIdentidad;
}
