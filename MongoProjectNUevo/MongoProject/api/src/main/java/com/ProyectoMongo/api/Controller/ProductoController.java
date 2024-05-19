package com.ProyectoMongo.api.Controller;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
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
    public ProductoModel getProductoById(@PathVariable String id) {
        return productoService.findProductoById(new ObjectId(id));
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
        ObjectId objectId = new ObjectId(id);
        ProductoModel deletedProducto = productoService.deleteProducto(objectId);
        if (deletedProducto != null) {
            return ResponseEntity.ok(deletedProducto);
        } else {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
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
}
