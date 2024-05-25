package com.ProyectoMongo.api.Model;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una promoción en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "promociones")
public class PromocionesModel {
    /**
     * Identificador único de la promoción.
     */
    @Id
    private ObjectId id;

    /**
     * Lista de productos asociados a la promoción.
     */
    private List<ProductoPromocionModel> productoPromocion;
}
