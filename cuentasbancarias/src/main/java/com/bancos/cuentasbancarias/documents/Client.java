package com.bancos.cuentasbancarias.documents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Data
@Document(collection = "cliente")
public class Client {
    @Id
    private ObjectId id;
    @NotEmpty
    private String nombre;

    private ClientType clientType;
    @Field("id")
    private List<ObjectId> cuentas=new ArrayList<>();
}
