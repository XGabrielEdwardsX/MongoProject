package com.ProyectoMongo.api.Model;
import java.util.Date;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPromocionModel {
    private String nombre;
    private ObjectId idProducto;
    private int descuento;
    private Date fechaInicio;
    private Date fechaFin;
}
