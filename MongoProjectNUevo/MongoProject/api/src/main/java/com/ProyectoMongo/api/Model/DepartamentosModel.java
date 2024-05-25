package com.ProyectoMongo.api.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DepartamentosModel representa un departamento en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente
 * los métodos getters, setters, el constructor sin argumentos y el
 * constructor con todos los argumentos.
 * También está mapeada a la colección "departamento" en MongoDB.
 * 
 * @see lombok.Data
 * @see lombok.NoArgsConstructor
 * @see lombok.AllArgsConstructor
 * @see org.springframework.data.mongodb.core.mapping.Document
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "departamento")
public class DepartamentosModel {
    
    /**
     * El identificador único del departamento, generado por MongoDB.
     */
    @Id
    private ObjectId id;
    
    /**
     * El nombre del departamento.
     * Se normaliza al asignarse: se trimea y se convierte a minúsculas.
     */
    private String nombre;
    
    /**
     * La lista de ciudades pertenecientes al departamento.
     * 
     * @see CiudadesModel
     */
    private List<CiudadesModel> ciudades;  

    /**
     * Establece el nombre del departamento.
     * El nombre se normaliza trimeando los espacios en blanco y convirtiéndolo a minúsculas.
     * 
     * @param nombre el nombre del departamento
     */
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.trim().toLowerCase(); // Normaliza el nombre
        }
    }
}
