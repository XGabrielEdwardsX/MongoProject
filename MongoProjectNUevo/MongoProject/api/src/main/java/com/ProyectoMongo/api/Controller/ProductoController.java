package com.ProyectoMongo.api.Controller;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ComentariosModel;
import com.ProyectoMongo.api.Model.ProductoModel;
import com.ProyectoMongo.api.Service.IProductoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas con los productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    /**
     * Obtener todos los productos.
     * @return Lista de todos los productos.
     */
    @GetMapping("/")
    public List<ProductoModel> getAllProductos() {
        return productoService.findAllProductos();
    }

    /**
     * Obtener un producto por su ID.
     * @param id ID del producto.
     * @return El producto correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable String id) {
        try {
            ProductoModel producto = productoService.findProductoById(new ObjectId(id));
            return ResponseEntity.ok(producto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Crear un nuevo producto.
     * @param producto Objeto del producto a crear.
     * @return El producto creado.
     */
    @PostMapping("/")
    public ResponseEntity<?> createProducto(@RequestBody ProductoModel producto) {
        try {
            ProductoModel savedProducto = productoService.saveProducto(producto);
            return ResponseEntity.ok(savedProducto);
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    /**
     * Eliminar un producto por su ID.
     * @param id ID del producto a eliminar.
     * @return El producto eliminado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            ProductoModel deletedProducto = productoService.deleteProducto(objectId);
            return ResponseEntity.ok(deletedProducto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Actualizar un producto existente.
     * @param id ID del producto a actualizar.
     * @param producto Objeto del producto con los datos actualizados.
     * @return El producto actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable String id, @RequestBody ProductoModel producto) {
        try {
            ObjectId objectId = new ObjectId(id);
            ProductoModel updatedProducto = productoService.updateProducto(objectId, producto);
            return ResponseEntity.ok(updatedProducto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    /**
     * Agregar un comentario a un producto.
     * @param id ID del producto.
     * @param comentario Objeto del comentario a agregar.
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/comentarios/{id}")
    public ResponseEntity<?> agregarComentario(@PathVariable String id, @RequestBody ComentariosModel comentario) {
        try {
            ObjectId idProducto = new ObjectId(id);
            ProductoModel agregado = productoService.agregarComentario(idProducto, comentario);
            if (agregado != null) {
                return ResponseEntity.ok("Comentario agregado exitosamente.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario no ha comprado este producto.");
            }
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
