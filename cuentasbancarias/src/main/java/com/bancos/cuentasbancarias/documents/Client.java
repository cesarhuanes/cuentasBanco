package com.bancos.cuentasbancarias.documents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    private ObjectId clientTypeId; // Almacena el ObjectId de ClientType
    private ClientType clientType; // Para almacenar el objeto ClientType después de la consulta

    @Field("id")
    private List<ObjectId> cuentas=new ArrayList<>();

    private  Perfil perfil;

}
