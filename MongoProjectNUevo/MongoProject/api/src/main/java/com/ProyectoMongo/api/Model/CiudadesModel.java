package com.ProyectoMongo.api.Model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase CiudadesModel representa una ciudad con su nombre y código postal.
 * Esta clase se mapea a una colección de MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CiudadesModel {
    
    /**
     * El nombre de la ciudad.
     */
    private String nombre;
    
    /**
     * El código postal de la ciudad.
     */
    private int codigoPostal;

    /**
     * Establece el nombre de la ciudad. Si el nombre no es nulo, lo normaliza
     * eliminando espacios en blanco y convirtiéndolo a minúsculas.
     *
     * @param nombre el nombre de la ciudad
     */
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.trim().toLowerCase(); // Normaliza el nombre
        }
    }
}
