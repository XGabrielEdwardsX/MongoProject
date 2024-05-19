package com.ProyectoMongo.api.Model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallesCompraModel { 
    private String tipo;
    private ObjectId idTipo;
    private long cantidad;
    private String talla;
    private String color;
    private String imagenPersonalizacion;
    private String detalles;
}

