package com.bancos.cuentasbancarias.documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="persona")
public class Person {
    @Id
    private ObjectId id;
    private String nombre;
    private String documentoIdentidad;
}
