package com.ProyectoMongo.api.Controller;

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

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    @Autowired
    private IComprasService comprasService;

    @GetMapping("/")
    public List<ComprasModel> getAllCompras() {
        return comprasService.findAllCompras();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompraById(@PathVariable String id) {
    try {
        ComprasModel compra = comprasService.findCompraById(new ObjectId(id));
        return ResponseEntity.ok(compra);
    } catch (RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompra(@PathVariable ObjectId id, @RequestBody ComprasModel compra) {
        try {
            ComprasModel updatedCompra = comprasService.updateCompra(id, compra);
            return ResponseEntity.ok(updatedCompra);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }  catch (StockInsuficienteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } 
    }
}
