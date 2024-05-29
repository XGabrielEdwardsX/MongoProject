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


 @Service
 public class ProductoServiceImpl implements IProductoService {
 
     @Autowired
     IProductoRepository productoRepository;
 
     @Autowired
     IPromocionesRepository promocionesRepository;
 
     @Autowired
     IComprasRepository compraRepository;
 
     @Autowired
     ComprasServiceImpl comprasServiceImpl;
 
     @Override
     public List<ProductoModel> findAllProductos() {
         return productoRepository.findAll();
     }
 
     @Override
     public ProductoModel findProductoById(ObjectId id) {
         ProductoModel producto = productoRepository.findById(id)
                 .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID: " + id + " no encontrado"));
 
         BigDecimal precioConDescuento = producto.getPrecioOriginal();
         if (precioConDescuento == null) {
             precioConDescuento = producto.getPrecio(); // Para productos antiguos que no tienen precioOriginal
         }
 
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
 
         if (updateNombre && productoRepository.existsByNombre(producto.getNombre())) {
             throw new RecursoYaExistenteException("Ya existe un producto con este nombre");
         }
 
         if (producto.getPrecioOriginal().compareTo(BigDecimal.ZERO) < 0) {
             throw new ValorInvalidoException("El precio original no puede ser negativo");
         }
 
         for (StockModel stock : producto.getStock()) {
             if (stock.getCantidad() < 0) {
                 throw new ValorInvalidoException("La cantidad no puede ser negativa");
             }
         }
 
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
 
         existingProducto.initializePrecio(); // Inicializar el precio basado en precioOriginal
         return productoRepository.save(existingProducto);
     }
 
      /* @Override
     public void agregarComentario(ObjectId idProducto, ComentariosModel comentario) {
         ProductoModel producto = productoRepository.findById(idProducto)
                 .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto));
 
         comentario.setPuedeComentar(comprasServiceImpl.usuarioHaCompradoProducto(comentario.getIdUsuario(), idProducto));
 
         if (!comentario.isPuedeComentar()) {
             throw new ValorInvalidoException("El usuario no ha comprado este producto y no puede comentar sobre él.");
         }
 
         List<ComentariosModel> comentarios = producto.getComentarios();
         comentarios.add(comentario);
         producto.setComentarios(comentarios);
 
         productoRepository.save(producto); */
}
 
 