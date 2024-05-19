package com.ProyectoMongo.api.Repository;
import com.ProyectoMongo.api.Model.UsuariosModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {
    boolean existsByEmail(String email);
    boolean existsByTelefono(Long telefono);
    boolean existsByRolesNombreUsuarioAndIdNot(String nombreUsuario, ObjectId id);
    boolean existsByRolesNombreUsuario(String nombreUsuario);
}