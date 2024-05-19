package com.ProyectoMongo.api.Controller;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Model.DepartamentosModel;
import com.ProyectoMongo.api.Service.IDepartamentosService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private IDepartamentosService departamentosService;

    @GetMapping("/")
    public List<DepartamentosModel> getAllDepartamentos() {
        return departamentosService.findAllDepartamentos();
    }

    @GetMapping("/{id}")
    public DepartamentosModel getDepartamentoById(@PathVariable String id) {
        return departamentosService.findDepartamentoById(new ObjectId(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> createDepartamento(@RequestBody DepartamentosModel departamento) {
        try {
            DepartamentosModel savedDepartamento = departamentosService.saveDepartamento(departamento);
            return ResponseEntity.ok(savedDepartamento);
        } catch (RecursoYaExistenteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

   @DeleteMapping("/{id}")
public ResponseEntity<?> deleteDepartamento(@PathVariable String id) {
    ObjectId objectId = new ObjectId(id);  // Convert String to ObjectId
    try {
        DepartamentosModel deleteDepartamento = departamentosService.deleteDepartamento(objectId);
        return ResponseEntity.ok(deleteDepartamento);
    } catch (RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

    @PutMapping("/{id}")
public ResponseEntity<?> updateDepartamento(@PathVariable ObjectId id, @RequestBody DepartamentosModel departamento) {
    try {
        DepartamentosModel updatedDepartamento = departamentosService.updateDepartamento(id, departamento);
        return ResponseEntity.ok(updatedDepartamento);
    } catch (RecursoYaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }   
}