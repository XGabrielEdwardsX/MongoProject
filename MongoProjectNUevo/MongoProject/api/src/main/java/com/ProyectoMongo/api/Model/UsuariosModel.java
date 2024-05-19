package com.ProyectoMongo.api.Model;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuario")
public class UsuariosModel {

    @Id
    private ObjectId id;
    private String nombre;
    private String email;
    private Long telefono;
    private Integer edad;
    private String genero; // "Hombre" o "Mujer"
    private List<DireccionesModel> direcciones; 
    private List<RolesModel> roles;

}
