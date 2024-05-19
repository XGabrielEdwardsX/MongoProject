package com.ProyectoMongo.api.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.PromocionesModel;
import com.ProyectoMongo.api.Service.IPromocionesService;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {
    @Autowired
    private IPromocionesService promocionesService;

    @GetMapping("/")
    public List<PromocionesModel> getAllPromos() {
        return promocionesService.findAllPromos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPromoById(@PathVariable String id) {
        try {
            PromocionesModel promo = promocionesService.findPromoById(new ObjectId(id));
            return ResponseEntity.ok(promo);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    
    @PostMapping("/")
    public ResponseEntity<?> createPromo(@RequestBody PromocionesModel promo) {
        try {
            PromocionesModel savedPromo = promocionesService.savepromo(promo);
            return ResponseEntity.ok(savedPromo);
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromo(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        PromocionesModel deletedPromo = promocionesService.deletePromo(objectId);
        if (deletedPromo != null) {
            return ResponseEntity.ok(deletedPromo);
        } else {
            throw new RecursoNoEncontradoException("Promoción no encontrada con ID: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePromo(@PathVariable ObjectId id, @RequestBody PromocionesModel promo) {
        try {
            PromocionesModel updatedPromo = promocionesService.updatePromo(id, promo);
            return ResponseEntity.ok(updatedPromo);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}
