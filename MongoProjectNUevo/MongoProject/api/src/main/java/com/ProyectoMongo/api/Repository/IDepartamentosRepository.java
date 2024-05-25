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

    /**
     * Verifica si existe un departamento con el nombre dado.
     * @param nombre Nombre del departamento a buscar.
     * @return Verdadero si existe un departamento con el nombre dado, falso de lo contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe al menos una ciudad asociada al departamento.
     * @param ciudad Lista de ciudades asociadas al departamento.
     * @return Verdadero si existe al menos una ciudad asociada al departamento, falso de lo contrario.
     */
    boolean existsByCiudades(List<CiudadesModel> ciudad);

    /**
     * Verifica si existe una ciudad con el nombre dado dentro del departamento.
     * @param nombre Nombre de la ciudad a buscar.
     * @return Verdadero si existe una ciudad con el nombre dado dentro del departamento, falso de lo contrario.
     */
    boolean existsByCiudadesNombre(String nombre);
    // boolean existsByCiudadesNombreAndIdNot(String nombre, ObjectId id);

    /**
     * Verifica si existe una ciudad con el código postal dado dentro del departamento.
     * @param codigoPostal Código postal de la ciudad a buscar.
     * @return Verdadero si existe una ciudad con el código postal dado dentro del departamento, falso de lo contrario.
     */
    boolean existsByCiudadesCodigoPostal(int codigoPostal);
    // boolean existsByCiudadesCodigoPostalAndIdNot(int codigoPostal, ObjectId id);
}
