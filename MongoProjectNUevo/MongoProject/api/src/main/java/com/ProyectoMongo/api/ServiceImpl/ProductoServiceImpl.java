package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ComentariosModel;
import com.ProyectoMongo.api.Model.ProductoModel;
import com.ProyectoMongo.api.Model.ProductoPromocionModel;
import com.ProyectoMongo.api.Model.PromocionesModel;
import com.ProyectoMongo.api.Model.StockModel;
import com.ProyectoMongo.api.Repository.IComprasRepository;
import com.ProyectoMongo.api.Repository.IProductoRepository;
import com.ProyectoMongo.api.Repository.IPromocionesRepository;
import com.ProyectoMongo.api.Repository.IComprasRepository;
import com.ProyectoMongo.api.Service.IProductoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import java.util.List;
import java.util.Optional;

/**
 * ProductoServiceImpl es la implementación del servicio de productos.
 * Este servicio maneja todas las operaciones relacionadas con los productos,
 * incluyendo la creación, lectura, actualización y eliminación de productos,
 * así como la gestión de comentarios y promociones.
 * 
 * @see IProductoService
 */

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;

    @Autowired
    IPromocionesRepository promocionesRepository;

    @Autowired
    IComprasRepository compraRepository;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD

    @Autowired
    ComprasServiceImpl comprasServiceImpl;

=======
>>>>>>> Stashed changes
    
    /**
    * Obtener todos los productos.
    * @return Lista de todos los productos.
    */
<<<<<<< Updated upstream
=======
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
    @Override
    public List<ProductoModel> findAllProductos() {
        return productoRepository.findAll();
    }

    /**
    * Obtener un producto por su ID.
    * @param id ID del producto.
    * @return El producto correspondiente al ID proporcionado.
    * @throws RecursoNoEncontradoException si el producto no es encontrado.
    * Este método verifica si el producto tiene promociones aplicables y ajusta su precio.
    */
    @Override
    public ProductoModel findProductoById(ObjectId id) {
        ProductoModel producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID: " + id + " no encontrado"));

<<<<<<< Updated upstream
         // Verifica si hay promociones aplicables al producto   
=======
<<<<<<< HEAD
        BigDecimal precioConDescuento = producto.getPrecioOriginal();
        if (precioConDescuento == null) {
            precioConDescuento = producto.getPrecio(); // Para productos antiguos que no tienen precioOriginal
        }

=======
         // Verifica si hay promociones aplicables al producto   
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
        List<PromocionesModel> promociones = promocionesRepository.findAll();
        Date now = new Date();
        for (PromocionesModel promocion : promociones) {
            for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                if (productoPromocion.getIdProducto().equals(id) &&
                    now.after(productoPromocion.getFechaInicio()) && now.before(productoPromocion.getFechaFin())) {
                    BigDecimal descuento = precioConDescuento.multiply(BigDecimal.valueOf(productoPromocion.getDescuento() / 100.0));
                    precioConDescuento = precioConDescuento.subtract(descuento);

                    if (producto.getPrecioOriginal() == null) {
                        producto.setPrecioOriginal(producto.getPrecio()); // Guardar el precio original solo una vez
                    }

                    break;
                }
            }
        }

        producto.setPrecio(precioConDescuento);
        productoRepository.save(producto);

        return producto;
    }

    /**
    * Guardar un nuevo producto.
    * @param producto Objeto de producto a guardar.
    * @return El producto guardado.
    * @throws RecursoYaExistenteException si el producto ya existe.
    * @throws ValorInvalidoException si el género del producto no es válido.
    * Este método valida que no existan productos duplicados y que el género del producto sea válido.
    */ 
    @Override
    public ProductoModel saveProducto(ProductoModel producto) {
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("El producto \"" + producto.getNombre() + "\" ya existe.");
        }

        if (!producto.getGenero().matches("Hombre|Mujer|Unisex")) {
            throw new ValorInvalidoException("Valor inválido para el género: " + producto.getGenero());
        }

        if (producto.getPrecioOriginal() == null) {
            throw new ValorInvalidoException("El precio original no puede ser nulo");
        }

        producto.initializePrecio(); // Inicializar el precio basado en precioOriginal

        for (StockModel stock : producto.getStock()) {
            if (stock.getCantidad() < 0) {
                throw new ValorInvalidoException("La cantidad no puede ser negativa");
            }
        }

        producto.setComentarios(null);

        return productoRepository.save(producto);
    }

    /**
    * Eliminar un producto por su ID.
    * @param id ID del producto a eliminar.
    * @return El producto eliminado.
    * @throws RecursoNoEncontradoException si el producto no es encontrado.
    */
    @Override
    public ProductoModel deleteProducto(ObjectId id) {
        Optional<ProductoModel> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            productoRepository.deleteById(id);
            return producto.get();
        }
        throw new RecursoNoEncontradoException("Producto con ID: " + id + " No encontrado");
    }

    /**
    * Actualizar un producto existente.
    * @param id ID del producto a actualizar.
    * @param producto Objeto de producto con los datos actualizados.
    * @return El producto actualizado.
    * @throws RecursoNoEncontradoException si el producto no es encontrado.
    * @throws RecursoYaExistenteException si ya existe un producto con el mismo nombre.
    * @throws ValorInvalidoException si el género del producto no es válido.
    * Este método actualiza los campos de un producto existente y valida posibles conflictos.
    */
    @Override
    public ProductoModel updateProducto(ObjectId id, ProductoModel producto) {
        ProductoModel existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (!existingProducto.getId().equals(producto.getId())) {
            throw new RecursoNoEncontradoException("El id no coincide");
        }

        boolean updateNombre = !existingProducto.getNombre().equals(producto.getNombre());

        if (updateNombre && productoRepository.existsByNombre(producto.getNombre())) {
            throw new RecursoYaExistenteException("Ya existe un producto con este nombre");
        }
<<<<<<< Updated upstream
=======
<<<<<<< HEAD

        if (producto.getPrecioOriginal().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorInvalidoException("El precio original no puede ser negativo");
        }

        for (StockModel stock : producto.getStock()) {
            if (stock.getCantidad() < 0) {
                throw new ValorInvalidoException("La cantidad no puede ser negativa");
            }
        }
=======
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes

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
        if (producto.getPrecioOriginal() != null) {
            existingProducto.setPrecioOriginal(producto.getPrecioOriginal());
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
            for (ComentariosModel comentario : producto.getComentarios()) {
                if (!comprasServiceImpl.usuarioHaCompradoProducto(comentario.getIdUsuario(), id)) {
                    throw new ValorInvalidoException("El usuario " + comentario.getIdUsuario() + " no ha comprado este producto y no puede comentar sobre él.");
                }
            }
            existingProducto.setComentarios(producto.getComentarios());
        }

        existingProducto.initializePrecio(); // Inicializar el precio basado en precioOriginal
        return productoRepository.save(existingProducto);
    }
    /**
    * Agregar un comentario a un producto.
    * @param idProducto ID del producto.
    * @param comentario Objeto de comentario a agregar.
    * @return El producto con el comentario agregado.
    * @throws RecursoNoEncontradoException si el producto o el usuario no son encontrados,
    * o si el usuario no ha comprado el producto.
    * Este método agrega un comentario a un producto solo si el usuario ha comprado el producto.
    */
    @Override
<<<<<<< Updated upstream
    public ProductoModel agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
        ProductoModel producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto));

        if (!usuarioHaCompradoProducto(comentario.getIdUsuario(), idProducto)) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + comentario.getIdUsuario() + " no ha comprado el producto con ID: " + idProducto);
=======
<<<<<<< HEAD
    public void agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
        ProductoModel producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto));

        comentario.setPuedeComentar(comprasServiceImpl.usuarioHaCompradoProducto(comentario.getIdUsuario(), idProducto));

        if (!comentario.isPuedeComentar()) {
            throw new ValorInvalidoException("El usuario no ha comprado este producto y no puede comentar sobre él.");
>>>>>>> Stashed changes
        }

        List<ComentariosModel> comentarios = producto.getComentarios();
        comentarios.add(comentario);
        producto.setComentarios(comentarios);

<<<<<<< Updated upstream
=======
        productoRepository.save(producto);
    }
}

      /* @Override
    public boolean agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
=======
    public ProductoModel agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
        ProductoModel producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto));

        if (!usuarioHaCompradoProducto(comentario.getIdUsuario(), idProducto)) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + comentario.getIdUsuario() + " no ha comprado el producto con ID: " + idProducto);
        }

        List<ComentariosModel> comentarios = producto.getComentarios();
        comentarios.add(comentario);
        producto.setComentarios(comentarios);

>>>>>>> Stashed changes
        return productoRepository.save(producto);
    }

    private boolean usuarioHaCompradoProducto(ObjectId usuarioId, ObjectId productoId) {
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        // Implementar la lógica para verificar si el usuario ha comprado el producto
        // Esto puede incluir llamadas a otros repositorios o servicios
        return true; // Cambiar esta línea por la lógica real */


=======
>>>>>>> Stashed changes
        return compraRepository.findAll().stream()
                .anyMatch(compra -> compra.getIdUsuario().equals(usuarioId) &&
                                    compra.getDetallesCompra().stream()
                                          .anyMatch(detalle -> detalle.getIdTipo().equals(productoId)));
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
