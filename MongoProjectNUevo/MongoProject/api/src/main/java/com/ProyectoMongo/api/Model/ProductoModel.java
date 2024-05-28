package com.ProyectoMongo.api.Model;

import java.util.List;
import java.math.BigDecimal;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un modelo de Producto en la base de datos MongoDB.
 */
@Data   
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "producto")
public class ProductoModel {

    /**
     * Identificador único del producto.
     */
    @Id
    private ObjectId id;

    /**
     * Nombre del producto.
     */
    private String nombre;

    /**
     * Categoría a la que pertenece el producto.
     */
    private String categoria;

    /**
     * Descripción detallada del producto.
     */
    private String descripcion;

    /**
     * Género del producto (puede ser utilizado para categorizar productos por género).
     */
    private String genero; 

    /**
     * Lista de imágenes asociadas al producto.
     */
    private List<ImagenModel> imagenes;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    private BigDecimal precioOriginal;
    private BigDecimal precio; // precio con descuento
=======
>>>>>>> Stashed changes

    /**
     * Precio del producto.
     */
    private BigDecimal precio;

    /**
     * Indica si el producto es parte de un paquete.
     */
<<<<<<< Updated upstream
=======
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
    private Boolean esPaquete;

    /**
     * Lista de productos incluidos si es un paquete.
     */
    private List<ProductosModel> productos;

    /**
     * Lista de stock disponible para el producto.
     */
    private List<StockModel> stock;

    /**
     * Lista de comentarios realizados sobre el producto.
     */
    private List<ComentariosModel> comentarios;

 // Método para inicializar el precio basado en precioOriginal si es nulo
    public void initializePrecio() {
    if (this.precio == null && this.precioOriginal != null) {
        this.precio = this.precioOriginal;
    }
}
}