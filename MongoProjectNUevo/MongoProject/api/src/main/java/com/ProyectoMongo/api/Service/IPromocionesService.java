package com.ProyectoMongo.api.Service;

import org.bson.types.ObjectId;
import com.ProyectoMongo.api.Model.PromocionesModel;
import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con las promociones.
 */
public interface IPromocionesService {

    /**
     * Obtiene todas las promociones.
     * @return Lista de todas las promociones.
     */
    List<PromocionesModel> findAllPromos();

    /**
     * Encuentra una promoción por su ID.
     * @param id ID de la promoción a buscar.
     * @return La promoción encontrada.
     */
    PromocionesModel findPromoById(ObjectId id);

    /**
     * Guarda una nueva promoción.
     * @param promocion La promoción a guardar.
     * @return La promoción guardada.
     */
    PromocionesModel savepromo(PromocionesModel promocion);

    /**
     * Elimina una promoción por su ID.
     * @param id ID de la promoción a eliminar.
     * @return La promoción eliminada.
     */
    PromocionesModel deletePromo(ObjectId id);

    /**
     * Actualiza una promoción existente.
     * @param id ID de la promoción a actualizar.
     * @param promocion La promoción con los datos actualizados.
     * @return La promoción actualizada.
     */
    PromocionesModel updatePromo(ObjectId id, PromocionesModel promocion);
}
