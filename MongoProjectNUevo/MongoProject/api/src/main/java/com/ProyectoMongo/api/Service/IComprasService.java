package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.ComprasModel;
import org.bson.types.ObjectId;

import java.util.List;

public interface IComprasService {
    List<ComprasModel> findAllCompras();
    ComprasModel findCompraById(ObjectId id);
    ComprasModel saveCompra(ComprasModel compra);
    ComprasModel updateCompra(ObjectId id, ComprasModel compra);
    ComprasModel deleteCompra(ObjectId id);
}
