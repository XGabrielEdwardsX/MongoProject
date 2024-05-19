package com.ProyectoMongo.api.Repository;


import com.ProyectoMongo.api.Model.UsuariosModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {

    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByTelefono(Long telefono);

}