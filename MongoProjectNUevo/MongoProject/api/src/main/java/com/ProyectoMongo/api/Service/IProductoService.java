package com.ProyectoMongo.api.Service;

import com.ProyectoMongo.api.Model.ProductoModel;
import org.bson.types.ObjectId;
import java.util.List;

public interface IProductoService {
    List<ProductoModel> findAllProductos();
    ProductoModel findProductoById(ObjectId id);
    ProductoModel saveProducto(ProductoModel producto);
    ProductoModel deleteProducto(ObjectId id);
    ProductoModel updateProducto(ObjectId id, ProductoModel producto);
}
