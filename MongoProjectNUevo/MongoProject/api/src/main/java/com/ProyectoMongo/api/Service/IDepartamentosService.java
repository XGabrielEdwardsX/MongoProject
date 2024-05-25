package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.DepartamentosModel;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con los departamentos.
 */
public interface IDepartamentosService {

    /**
     * Obtiene todos los departamentos.
     * @return Lista de todos los departamentos.
     */
    List<DepartamentosModel> findAllDepartamentos();

    /**
     * Encuentra un departamento por su ID.
     * @param id ID del departamento a buscar.
     * @return El departamento encontrado.
     */
    DepartamentosModel findDepartamentoById(ObjectId id);

    /**
     * Guarda un nuevo departamento.
     * @param departamento El departamento a guardar.
     * @return El departamento guardado.
     */
    DepartamentosModel saveDepartamento(DepartamentosModel departamento);

    /**
     * Elimina un departamento por su ID.
     * @param objectId ID del departamento a eliminar.
     * @return El departamento eliminado.
     */
    DepartamentosModel deleteDepartamento(ObjectId objectId);

    /**
     * Actualiza un departamento existente.
     * @param id ID del departamento a actualizar.
     * @param departamento El departamento con los datos actualizados.
     * @return El departamento actualizado.
     */
    DepartamentosModel updateDepartamento(ObjectId id, DepartamentosModel departamento);
}
