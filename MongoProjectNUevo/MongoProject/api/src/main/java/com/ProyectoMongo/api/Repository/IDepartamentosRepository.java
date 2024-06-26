package com.ProyectoMongo.api.Repository;

import com.ProyectoMongo.api.Model.CiudadesModel;
import com.ProyectoMongo.api.Model.DepartamentosModel;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad DepartamentosModel.
 */
@Repository
public interface IDepartamentosRepository extends MongoRepository<DepartamentosModel, ObjectId> {

    boolean existsByNombre(String nombre);

  
    boolean existsByCiudades(List<CiudadesModel> ciudad);

 
    boolean existsByCiudadesNombre(String nombre);


    boolean existsByCiudadesCodigoPostal(int codigoPostal);
}
