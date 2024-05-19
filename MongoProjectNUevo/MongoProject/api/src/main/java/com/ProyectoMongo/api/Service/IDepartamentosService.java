package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.DepartamentosModel;
import org.bson.types.ObjectId;
import java.util.List;

public interface IDepartamentosService {
    List<DepartamentosModel> findAllDepartamentos();
    DepartamentosModel findDepartamentoById(ObjectId id);
    DepartamentosModel saveDepartamento(DepartamentosModel departamento);
    DepartamentosModel deleteDepartamento(ObjectId objectId);
    DepartamentosModel updateDepartamento(ObjectId id, DepartamentosModel departamento);
}
