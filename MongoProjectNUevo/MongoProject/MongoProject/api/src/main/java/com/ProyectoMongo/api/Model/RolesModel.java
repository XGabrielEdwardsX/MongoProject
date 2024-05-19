package com.ProyectoMongo.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesModel {
    
    private String nombreUsuario;
    private String rol; 
    private String clave;


}
