package com.ProyectoMongo.api.Model;

import java.math.BigDecimal;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DetallesCompraModel representa los detalles de una compra en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente
 * los métodos getters, setters, el constructor sin argumentos y el
 * constructor con todos los argumentos.
 * 
 * @see lombok.Data
 * @see lombok.NoArgsConstructor
 * @see lombok.AllArgsConstructor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallesCompraModel { 

    /**
     * El tipo de producto.
     */
    private String tipo;

    /**
     * El identificador único del tipo de producto, generado por MongoDB.
     */
    private ObjectId idTipo;

    /**
     * La cantidad de productos.
     */
    private long cantidad;

    /**
     * La talla del producto.
     */
    private String talla;

    /**
     * El color del producto.
     */
    private String color;

    /**
     * La imagen personalizada del producto.
     */
    private String imagenPersonalizada;

    /**
     * Los detalles adicionales del producto.
     */
    private String detalles;

    /**
     * El precio del producto.
     */
    private BigDecimal precio;
}
