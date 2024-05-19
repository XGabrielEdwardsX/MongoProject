package com.ProyectoMongo.api.Model;

import java.util.List;
import java.math.BigDecimal;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "producto")
public class ProductoModel {

    @Id
    private ObjectId id;

    private String nombre;
    private String categoria;
    private String descripcion;
    private String genero; 
    private String imagen;
    private BigDecimal precio;
    private Boolean esPaquete;
    private List<ProductosModel> productos;
    private List<StockModel> stock;
    private List<ComentariosModel> comentarios;
}
