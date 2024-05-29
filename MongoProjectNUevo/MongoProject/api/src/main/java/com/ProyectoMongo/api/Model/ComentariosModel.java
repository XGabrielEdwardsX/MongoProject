package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
// import java.sql.Date;
import java.util.Date;

/**
 * ComentariosModel representa un comentario en el sistema.
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
public class ComentariosModel {
    
    /**
     * El identificador del usuario que realizó el comentario.
     * Es un ObjectId generado por MongoDB.
     */
    private ObjectId idUsuario;
    
    /**
     * El texto del comentario.
     */
    private String texto;
    
    /**
     * La fecha en la que se realizó el comentario.
     */
    private Date fecha;
}



