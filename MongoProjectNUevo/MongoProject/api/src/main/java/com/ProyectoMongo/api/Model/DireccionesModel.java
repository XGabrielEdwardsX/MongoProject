package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DireccionesModel representa una dirección en el sistema.
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
public class DireccionesModel {

    /**
     * El código postal de la ciudad correspondiente a la dirección.
     */
    private int codigoPostalCiudad;

    /**
     * La dirección física.
     */
    private String direccion; 

    /**
     * Detalles adicionales de la dirección.
     */
    private String detalles;

}
