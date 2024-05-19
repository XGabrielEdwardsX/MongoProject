package com.ProyectoMongo.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionesModel {

    private int codigoPostalCiudad;
    private String direccion; 
    private String detalles;

}
