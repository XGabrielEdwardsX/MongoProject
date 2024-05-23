package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ComentariosModel;
import com.ProyectoMongo.api.Model.ProductoModel;
import com.ProyectoMongo.api.Model.ProductoPromocionModel;
import com.ProyectoMongo.api.Model.PromocionesModel;
import com.ProyectoMongo.api.Repository.IProductoRepository;
import com.ProyectoMongo.api.Repository.IPromocionesRepository;
import com.ProyectoMongo.api.Service.IProductoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;
    
    @Autowired
    IPromocionesRepository promocionesRepository;

    @Override
    public List<ProductoModel> findAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoModel findProductoById(ObjectId id) {
        ProductoModel producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID: " + id + " no encontrado"));

        // Buscar promociones activas para este producto
        List<PromocionesModel> promociones = promocionesRepository.findAll();
        Date now = new Date();
        for (PromocionesModel promocion : promociones) {
            for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                if (productoPromocion.getIdProducto().equals(id) &&
                    now.after(productoPromocion.getFechaInicio()) && now.before(productoPromocion.getFechaFin())) {
                    BigDecimal descuento = producto.getPrecio().multiply(BigDecimal.valueOf(productoPromocion.getDescuento() / 100.0));
                    producto.setPrecio(producto.getPrecio().subtract(descuento));
                    break;
                }
            }
        }

        return producto;
    }

    @Override
    public ProductoModel saveProducto(ProductoModel producto) {
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("El producto \"" + producto.getNombre() + "\" ya existe.");
        }

        if (!producto.getGenero().matches("Hombre|Mujer|Unisex")) {
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
            throw new RecursoNoEncontradoException("El id no coincide");
        }

        boolean updateNombre = !existingProducto.getNombre().equals(producto.getNombre());
        // No asignar hasta la explicación de Juan Jacobo
        // boolean updateCategoria = !existingProducto.getCategoria().equals(producto.getCategoria());

        if (updateNombre && productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("Ya existe un producto con este nombre");
        }
        /* // No asignar hasta la explicación de Juan Jacobo
        if (updateCategoria && productoRepository.existsByCategoria(producto.getCategoria())) {
            throw new RecursoYaExistenteException("El producto ya pertenece a una categoría");
        }
        */

        if (producto.getNombre() != null) {
            existingProducto.setNombre(producto.getNombre());
        }
        if (producto.getCategoria() != null) {
            existingProducto.setCategoria(producto.getCategoria());
        }
        if (producto.getDescripcion() != null) {
            existingProducto.setDescripcion(producto.getDescripcion());
        }
        if (producto.getGenero() != null) {
            if (!producto.getGenero().matches("Hombre|Mujer|Unisex")) {
                throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
            }
            existingProducto.setGenero(producto.getGenero());
        }
        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            existingProducto.setImagenes(producto.getImagenes());
        }
        if (producto.getPrecio() != null) {
            existingProducto.setPrecio(producto.getPrecio());
        }
        if (producto.getEsPaquete() != null) {
            existingProducto.setEsPaquete(producto.getEsPaquete());
        }
        if (producto.getProductos() != null && !producto.getProductos().isEmpty()) {
            existingProducto.setProductos(producto.getProductos());
        }
        if (producto.getStock() != null && !producto.getStock().isEmpty()) {
            existingProducto.setStock(producto.getStock());
        }
        if (producto.getComentarios() != null && !producto.getComentarios().isEmpty()) {
            existingProducto.setComentarios(producto.getComentarios());
        }
    
        return productoRepository.save(existingProducto);
    }

    @Override
    public boolean agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agregarComentario'");
    }

      /* @Override
    public boolean agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
        ProductoModel producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto));

        // Aquí debes verificar si el usuario ha comprado el producto
        // Este ejemplo asume que existe un método usuarioHaCompradoProducto
       if (!usuarioHaCompradoProducto(comentario.getUsuarioId(), idProducto)) {
            return false;
        }

        List<ComentariosModel> comentarios = producto.getComentarios();
        comentarios.add(comentario);
        producto.setComentarios(comentarios);

        productoRepository.save(producto);
        return true;
    }

    private boolean usuarioHaCompradoProducto(ObjectId usuarioId, ObjectId productoId) {
        // Implementar la lógica para verificar si el usuario ha comprado el producto
        // Esto puede incluir llamadas a otros repositorios o servicios
        return true; // Cambiar esta línea por la lógica real */
}

