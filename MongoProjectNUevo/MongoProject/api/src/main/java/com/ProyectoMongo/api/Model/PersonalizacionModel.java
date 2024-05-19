package com.ProyectoMongo.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizacionModel {
    private String imagenPersonalizada;
    private String colorPersonalizado;
    private String detalles;
}