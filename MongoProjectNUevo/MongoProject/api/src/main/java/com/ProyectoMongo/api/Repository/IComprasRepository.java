package com.ProyectoMongo.api.Repository;

import com.ProyectoMongo.api.Model.ComprasModel;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad ComprasModel.
 */
@Repository
public interface IComprasRepository extends MongoRepository<ComprasModel, ObjectId> {
    List<ComprasModel> findByIdUsuario(ObjectId idUsuario);

}
