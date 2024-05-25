package com.ProyectoMongo.api.Service;

import org.bson.types.ObjectId;
import com.ProyectoMongo.api.Model.UsuariosModel;
import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con los usuarios.
 */
public interface IUsuariosService {

    /**
     * Obtiene todos los usuarios.
     * @return Lista de todos los usuarios.
     */
    List<UsuariosModel> findAllUsuarios();

    /**
     * Encuentra un usuario por su ID.
     * @param id ID del usuario a buscar.
     * @return El usuario encontrado.
     */
    UsuariosModel findUsuarioById(ObjectId id);

    /**
     * Guarda un nuevo usuario.
     * @param usuario El usuario a guardar.
     * @return El usuario guardado.
     */
    UsuariosModel saveUsuario(UsuariosModel usuario);

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar.
     * @return El usuario eliminado.
     */
    UsuariosModel deleteUsuario(ObjectId id);

    /**
     * Actualiza un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param usuario El usuario con los datos actualizados.
     * @return El usuario actualizado.
     */
    UsuariosModel updateUsuario(ObjectId id, UsuariosModel usuario);
}
