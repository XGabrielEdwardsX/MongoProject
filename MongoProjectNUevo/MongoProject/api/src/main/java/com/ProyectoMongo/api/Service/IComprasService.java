package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.ComprasModel;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con las compras.
 */
public interface IComprasService {

    /**
     * Obtiene todas las compras.
     * @return Lista de todas las compras.
     */
    List<ComprasModel> findAllCompras();

    /**
     * Encuentra una compra por su ID.
     * @param id ID de la compra a buscar.
     * @return La compra encontrada.
     */
    ComprasModel findCompraById(ObjectId id);

    /**
     * Guarda una nueva compra.
     * @param compra La compra a guardar.
     * @return La compra guardada.
     */
    ComprasModel saveCompra(ComprasModel compra);

    /**
     * Actualiza una compra existente.
     * @param id ID de la compra a actualizar.
     * @param compra La compra con los datos actualizados.
     * @return La compra actualizada.
     */
    ComprasModel updateCompra(ObjectId id, ComprasModel compra);

    /**
     * Elimina una compra por su ID.
     * @param id ID de la compra a eliminar.
     * @return La compra eliminada.
     */
    ComprasModel deleteCompra(ObjectId id);
}
