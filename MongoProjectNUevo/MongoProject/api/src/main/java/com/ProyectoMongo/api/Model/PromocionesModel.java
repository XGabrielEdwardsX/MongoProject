package com.ProyectoMongo.api.Model;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "promociones")
public class PromocionesModel {
    @Id
    private ObjectId id;
    private List<ProductoPromocionModel> productoPromocion;
}
