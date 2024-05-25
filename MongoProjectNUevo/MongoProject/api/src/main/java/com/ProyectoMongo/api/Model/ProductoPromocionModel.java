package com.ProyectoMongo.api.Model;

import java.util.Date;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una promoción aplicada a un producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPromocionModel {
    
    /**
     * Nombre de la promoción.
     */
    private String nombre;
    
    /**
     * Identificador del producto al que se aplica la promoción.
     */
    private ObjectId idProducto;
    
    /**
     * Descuento en porcentaje que se aplica al producto.
     */
    private int descuento;
    
    /**
     * Fecha de inicio de la promoción.
     */
    private Date fechaInicio;
    
    /**
     * Fecha de finalización de la promoción.
     */
    private Date fechaFin;
}
