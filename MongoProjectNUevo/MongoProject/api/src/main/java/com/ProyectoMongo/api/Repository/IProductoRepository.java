package com.ProyectoMongo.api.Repository;

import com.ProyectoMongo.api.Model.ProductoModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends MongoRepository<ProductoModel, ObjectId> {
    boolean existsByNombre(String nombre);
    boolean existsByCategoria(String categoria);
}
