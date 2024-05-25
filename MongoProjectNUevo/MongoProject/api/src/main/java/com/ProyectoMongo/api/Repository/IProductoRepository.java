package com.ProyectoMongo.api.Repository;

import com.ProyectoMongo.api.Model.ProductoModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad ProductoModel.
 */
@Repository
public interface IProductoRepository extends MongoRepository<ProductoModel, ObjectId> {

    /**
     * Verifica si existe un producto con el nombre dado.
     * @param nombre Nombre del producto a buscar.
     * @return Verdadero si existe un producto con el nombre dado, falso de lo contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe al menos un producto en la categoría dada.
     * @param categoria Categoría de productos a buscar.
     * @return Verdadero si existe al menos un producto en la categoría dada, falso de lo contrario.
     */
    boolean existsByCategoria(String categoria);
}
