package com.ProyectoMongo.api.Model;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Clase que representa un usuario en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuario")
public class UsuariosModel {
    /**
     * Identificador único del usuario.
     */
    @Id
    private ObjectId id;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Dirección de correo electrónico del usuario.
     */
    private String email;

    /**
     * Fecha de nacimiento del usuario.
     */
    private Date fechaNacimiento;

    /**
     * Número de teléfono del usuario.
     */
    private Long telefono;

    /**
     * Edad del usuario.
     */
    private Integer edad;

    /**
     * Género del usuario (puede ser "Hombre" o "Mujer").
     */
    private String genero;

    /**
     * Lista de direcciones asociadas al usuario.
     */
    private List<DireccionesModel> direcciones; 

    /**
     * Lista de roles asignados al usuario.
     */
    private List<RolesModel> roles;
}
