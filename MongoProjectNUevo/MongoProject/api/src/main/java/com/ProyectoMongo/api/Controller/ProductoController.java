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

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/")
    public List<ProductoModel> getAllProductos() {
        return productoService.findAllProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable String id) {
        try {
            ProductoModel producto = productoService.findProductoById(new ObjectId(id));
            return ResponseEntity.ok(producto);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createProducto(@RequestBody ProductoModel producto) {
        try {
            ProductoModel savedProducto = productoService.saveProducto(producto);
            return ResponseEntity.ok(savedProducto);
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

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

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable String id, @RequestBody ComentariosModel comentario) {
        ObjectId idProducto = new ObjectId(id);
        boolean agregado = productoService.agregarComentario(idProducto, comentario);
        if (agregado) {
            return ResponseEntity.ok("Comentario agregado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario no ha comprado este producto.");
        }
    }
}
