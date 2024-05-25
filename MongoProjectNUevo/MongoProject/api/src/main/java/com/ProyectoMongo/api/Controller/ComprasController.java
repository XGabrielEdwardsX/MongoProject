package com.ProyectoMongo.api.Controller;

import com.ProyectoMongo.api.Exception.CompraActivaException;
import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.StockInsuficienteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ComprasModel;
import com.ProyectoMongo.api.Service.IComprasService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas con las compras.
 */
@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    @Autowired
    private IComprasService comprasService;

    /**
     * Obtener todas las compras.
     * @return Lista de todas las compras.
     */
    @GetMapping("/")
    public List<ComprasModel> getAllCompras() {
        return comprasService.findAllCompras();
    }

    /**
     * Obtener una compra por su ID.
     * @param id ID de la compra.
     * @return La compra correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompraById(@PathVariable String id) {
        try {
            ComprasModel compra = comprasService.findCompraById(new ObjectId(id));
            return ResponseEntity.ok(compra);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Crear una nueva compra.
     * @param compra Objeto de compra a crear.
     * @return La compra creada.
     */
    @PostMapping("/")
    public ResponseEntity<?> createCompra(@RequestBody ComprasModel compra) {
        try {
            ComprasModel savedCompra = comprasService.saveCompra(compra);
            return ResponseEntity.ok(savedCompra);
        } catch (ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (StockInsuficienteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } 
    }

    /**
     * Eliminar una compra por su ID.
     * @param id ID de la compra a eliminar.
     * @return La compra eliminada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompra(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        ComprasModel deletedCompra = comprasService.deleteCompra(objectId);
        if (deletedCompra != null) {
            return ResponseEntity.ok(deletedCompra);
        } else {
            throw new RecursoNoEncontradoException("Compra no encontrada con ID: " + id);
        }
    }

    /**
     * Actualizar una compra existente.
     * @param id ID de la compra a actualizar.
     * @param compra Objeto de compra con los datos actualizados.
     * @return La compra actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompra(@PathVariable String id, @RequestBody ComprasModel compra) {
        try {
            ObjectId objectId = new ObjectId(id);
            ComprasModel updatedCompra = comprasService.updateCompra(objectId, compra);
            return ResponseEntity.ok(updatedCompra);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (StockInsuficienteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (CompraActivaException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}