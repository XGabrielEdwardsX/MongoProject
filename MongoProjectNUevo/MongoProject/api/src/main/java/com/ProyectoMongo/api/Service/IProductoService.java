package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.ComentariosModel;
import com.ProyectoMongo.api.Model.ProductoModel;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Interfaz que define los métodos para el servicio relacionado con los productos.
 */
public interface IProductoService {
    List<ProductoModel> findAllProductos();
    ProductoModel findProductoById(ObjectId id);
    ProductoModel saveProducto(ProductoModel producto);
    ProductoModel deleteProducto(ObjectId id);
    ProductoModel updateProducto(ObjectId id, ProductoModel producto);

}
