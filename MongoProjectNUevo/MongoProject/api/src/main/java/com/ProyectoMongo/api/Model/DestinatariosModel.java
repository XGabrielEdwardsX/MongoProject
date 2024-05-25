package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DestinatariosModel representa un destinatario en el sistema.
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
public class DestinatariosModel {
    
    /**
     * El nombre del destinatario.
     */
    private String nombre;
    
    /**
     * El apellido del destinatario.
     */
    private String apellido;
    
    /**
     * El código postal de la ciudad del destinatario.
     */
    private long codigoPostalCiudad;
    
    /**
     * La dirección del destinatario.
     */
    private String direccion;
    
    /**
     * Detalles adicionales de la dirección del destinatario.
     */
    private String detallesDireccion;
    
    /**
     * El número de teléfono del destinatario.
     */
    private long telefono;
}
