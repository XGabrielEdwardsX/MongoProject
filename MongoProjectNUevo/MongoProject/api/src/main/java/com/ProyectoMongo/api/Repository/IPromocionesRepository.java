package com.ProyectoMongo.api.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ProyectoMongo.api.Model.PromocionesModel;

@Repository
public interface IPromocionesRepository extends MongoRepository<PromocionesModel, ObjectId> {
    
}