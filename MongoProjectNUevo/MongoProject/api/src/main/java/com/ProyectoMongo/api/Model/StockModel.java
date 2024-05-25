package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa el stock de un producto en diferentes tallas y colores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockModel {
    /**
     * Talla del producto en el stock.
     */
    private String talla;

    /**
     * Color del producto en el stock.
     */
    private String color;

    /**
     * Cantidad disponible del producto en el stock.
     */
    private Integer cantidad;
}
