package com.ProyectoMongo.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesModel {
    @Indexed(unique = true)
    private String nombreUsuario;
    private String rol; 
    private String clave;
}
