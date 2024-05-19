package com.ProyectoMongo.api.Service;

import org.bson.types.ObjectId;

import com.ProyectoMongo.api.Model.PromocionesModel;

import java.util.List;

public interface IPromocionesService {
    List<PromocionesModel> findAllPromos();
    PromocionesModel findPromoById(ObjectId id);
    PromocionesModel savepromo(PromocionesModel usuario);
    PromocionesModel deletePromo(ObjectId id);
    PromocionesModel updatePromo(ObjectId id, PromocionesModel usuario);
}