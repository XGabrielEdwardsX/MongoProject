package com.ProyectoMongo.api.Repository;

import com.ProyectoMongo.api.Model.UsuariosModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad UsuariosModel.
 */
@Repository
public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     * @param email Correo electrónico del usuario a buscar.
     * @return Verdadero si existe un usuario con el correo electrónico dado, falso de lo contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el número de teléfono dado.
     * @param telefono Número de teléfono del usuario a buscar.
     * @return Verdadero si existe un usuario con el número de teléfono dado, falso de lo contrario.
     */
    boolean existsByTelefono(Long telefono);

    /**
     * Verifica si existe un usuario con el correo electrónico dado y con un ID diferente al proporcionado.
     * @param email Correo electrónico del usuario a buscar.
     * @param id ID del usuario a excluir de la búsqueda.
     * @return Verdadero si existe un usuario con el correo electrónico dado y un ID diferente al proporcionado, falso de lo contrario.
     */
    boolean existsByEmailAndIdNot(String email, ObjectId id);

    /**
     * Verifica si existe un usuario con el número de teléfono dado y con un ID diferente al proporcionado.
     * @param telefono Número de teléfono del usuario a buscar.
     * @param id ID del usuario a excluir de la búsqueda.
     * @return Verdadero si existe un usuario con el número de teléfono dado y un ID diferente al proporcionado, falso de lo contrario.
     */
    boolean existsByTelefonoAndIdNot(Long telefono, ObjectId id);

    /**
     * Verifica si existe un usuario con el nombre de usuario del rol dado y con un ID diferente al proporcionado.
     * @param nombreUsuario Nombre de usuario del rol del usuario a buscar.
     * @param id ID del usuario a excluir de la búsqueda.
     * @return Verdadero si existe un usuario con el nombre de usuario del rol dado y un ID diferente al proporcionado, falso de lo contrario.
     */
    boolean existsByRolesNombreUsuarioAndIdNot(String nombreUsuario, ObjectId id);

    /**
     * Verifica si existe al menos un usuario con el nombre de usuario del rol dado.
     * @param nombreUsuario Nombre de usuario del rol del usuario a buscar.
     * @return Verdadero si existe al menos un usuario con el nombre de usuario del rol dado, falso de lo contrario.
     */
    boolean existsByRolesNombreUsuario(String nombreUsuario);
}
