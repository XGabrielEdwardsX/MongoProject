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
    private List<ImagenModel> imagenes;
    private BigDecimal precioOriginal;
    private BigDecimal precio; // Este será el precio con descuento si aplica, o el precio original si no hay descuento
    private Boolean esPaquete;
    private List<ProductosModel> productos;
    private List<StockModel> stock;
    private List<ComentariosModel> comentarios;

    // Método para inicializar el precio basado en precioOriginal si es nulo
    public void initializePrecio() {
        if (this.precio == null && this.precioOriginal != null) {
            this.precio = this.precioOriginal;
        }
    }
}
