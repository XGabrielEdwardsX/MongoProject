package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Clase que representa los roles de usuario en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesModel {
    /**
     * Nombre de usuario, utilizado como identificador único.
     */
    @Indexed(unique = true)
    private String nombreUsuario;

    /**
     * Rol del usuario en el sistema.
     */
    private String rol;

    /**
     * Clave o contraseña asociada al usuario.
     */
    private String clave;
}
