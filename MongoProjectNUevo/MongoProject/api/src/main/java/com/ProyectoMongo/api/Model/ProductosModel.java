package com.ProyectoMongo.api.Model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un producto en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosModel {

    /**
     * Identificador único del producto.
     */
    private ObjectId idProducto;

    /**
     * Cantidad disponible del producto.
     */
    private Long cantidad;
}
