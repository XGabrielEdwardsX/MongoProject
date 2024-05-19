package com.ProyectoMongo.api.Model;
import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosModel {
    
    private ObjectId idProducto;
    private Long cantidad;
    private PersonalizacionModel personalizacion;
}
