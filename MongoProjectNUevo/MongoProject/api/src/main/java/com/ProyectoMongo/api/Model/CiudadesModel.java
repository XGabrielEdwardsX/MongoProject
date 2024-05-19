package com.ProyectoMongo.api.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CiudadesModel {
    
    private String nombre;
    private int codigoPostal;

    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.trim().toLowerCase(); // Normaliza el nombre
}
    }
    
}
