package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ImagenModel representa una imagen con su diseño en el sistema.
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
public class ImagenModel {
    
    /**
     * El diseño de la imagen.
     */
    private String diseno;
    
    /**
     * La imagen en formato base64 o la URL de la imagen.
     */
    private String imagen;
}
