package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.ComentariosModel;
import com.ProyectoMongo.api.Model.ProductoModel;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con los productos.
 */
public interface IProductoService {

    /**
     * Obtiene todos los productos.
     * @return Lista de todos los productos.
     */
    List<ProductoModel> findAllProductos();

    /**
     * Encuentra un producto por su ID.
     * @param id ID del producto a buscar.
     * @return El producto encontrado.
     */
    ProductoModel findProductoById(ObjectId id);

    /**
     * Guarda un nuevo producto.
     * @param producto El producto a guardar.
     * @return El producto guardado.
     */
    ProductoModel saveProducto(ProductoModel producto);

    /**
     * Elimina un producto por su ID.
     * @param id ID del producto a eliminar.
     * @return El producto eliminado.
     */
    ProductoModel deleteProducto(ObjectId id);

    /**
     * Actualiza un producto existente.
     * @param id ID del producto a actualizar.
     * @param producto El producto con los datos actualizados.
     * @return El producto actualizado.
     */
    ProductoModel updateProducto(ObjectId id, ProductoModel producto);
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    void agregarComentario(ObjectId idProducto, ComentariosModel comentario);
=======
>>>>>>> Stashed changes

    /**
     * Agrega un comentario a un producto existente.
     * @param idProducto ID del producto al que se va a agregar el comentario.
     * @param comentario El comentario a agregar.
     * @return El producto con el comentario agregado.
     */
    ProductoModel agregarComentario(ObjectId idProducto, ComentariosModel comentario);
<<<<<<< Updated upstream
=======
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
}
