package com.bancos.cuentasbancarias.documents;

import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Data
@Document(collection = "cliente")
public class Cliente {
    @Id
    private ObjectId id;
    @NotEmpty
    private String nombre;

    private ClientType clientType;
    @Field("id")
    private List<ObjectId> cuentas=new ArrayList<>();
}
