package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ProductoModel;
import com.ProyectoMongo.api.Repository.IProductoRepository;
import com.ProyectoMongo.api.Service.IProductoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;

    @Override
    public List<ProductoModel> findAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoModel findProductoById(ObjectId id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID: " + id + " no encontrado"));
    }

    @Override
    public ProductoModel saveProducto(ProductoModel producto) {
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("El producto ya existe.");
        }
        if (productoRepository.existsByCategoria(producto.getCategoria())) {
            throw new RecursoYaExistenteException("El producto ya tiene una categoría asignada");
        }

        try {
            if (!producto.getGenero().matches("Hombre|Mujer|Unisex")) {
                throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
            }
        } catch (IllegalArgumentException e) {
            throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
        }

        return productoRepository.save(producto);
    }

    @Override
    public ProductoModel deleteProducto(ObjectId id) {
        Optional<ProductoModel> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            productoRepository.deleteById(id);
            return producto.get();
        }
        throw new RecursoNoEncontradoException("Producto con ID: " + id + " No encontrado");
    }

    @Override
    public ProductoModel updateProducto(ObjectId id, ProductoModel producto) {
        ProductoModel existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (!existingProducto.getId().equals(producto.getId())) {
            throw new RecursoNoEncontradoException("El id del producto no coincide con sus datos");
        }

        boolean updateNombre = !existingProducto.getNombre().equals(producto.getNombre());
        boolean updateCategoria = !existingProducto.getCategoria().equals(producto.getCategoria());

        if (updateNombre && productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("Ya existe un producto con este nombre");
        }
        if (updateCategoria && productoRepository.existsByCategoria(producto.getCategoria())) {
            throw new RecursoYaExistenteException("El producto ya pertenece a una categoría");
        }

        existingProducto.setNombre(producto.getNombre());
        existingProducto.setCategoria(producto.getCategoria());
        existingProducto.setDescripcion(producto.getDescripcion());
        existingProducto.setGenero(producto.getGenero());
        existingProducto.setImagen(producto.getImagen());
        existingProducto.setPrecio(producto.getPrecio());
        existingProducto.setEsPaquete(producto.getEsPaquete());
        existingProducto.setProductos(producto.getProductos());
        existingProducto.setStock(producto.getStock());
        existingProducto.setComentarios(producto.getComentarios());

        try {
            if (!producto.getGenero().matches("Hombre|Mujer|Unisex")) {
                throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
            }
        } catch (IllegalArgumentException e) {
            throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
        }

        return productoRepository.save(existingProducto);
    }
}
