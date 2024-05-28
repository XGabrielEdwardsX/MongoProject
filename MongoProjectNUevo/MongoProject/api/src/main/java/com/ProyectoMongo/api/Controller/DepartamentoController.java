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

/**
 * Controlador REST para manejar las operaciones relacionadas con los departamentos.
 */
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private IDepartamentosService departamentosService;

    /**
     * Obtener todos los departamentos.
     * @return Lista de todos los departamentos.
     */
    @GetMapping("/")
    public List<DepartamentosModel> getAllDepartamentos() {
        return departamentosService.findAllDepartamentos();
    }

    /**
     * Obtener un departamento por su ID.
     * @param id ID del departamento.
     * @return El departamento correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartamentoById(@PathVariable String id) {
        try {
            DepartamentosModel departamento = departamentosService.findDepartamentoById(new ObjectId(id));
            return ResponseEntity.ok(departamento);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Crear un nuevo departamento.
     * @param departamento Objeto del departamento a crear.
     * @return El departamento creado.
     */
    @PostMapping("/")
    public ResponseEntity<?> createDepartamento(@RequestBody DepartamentosModel departamento) {
        try {
            DepartamentosModel savedDepartamento = departamentosService.saveDepartamento(departamento);
            return ResponseEntity.ok(savedDepartamento);
        } catch (RecursoYaExistenteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch(RecursoNoEncontradoException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
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
=======
>>>>>>> Stashed changes
    /**
     * Eliminar un departamento por su ID.
     * @param id ID del departamento a eliminar.
     * @return El departamento eliminado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartamento(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);  // Convertir String a ObjectId
        try {
            DepartamentosModel deleteDepartamento = departamentosService.deleteDepartamento(objectId);
            return ResponseEntity.ok(deleteDepartamento);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
<<<<<<< Updated upstream
=======
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes

    /**
     * Actualizar un departamento existente.
     * @param id ID del departamento a actualizar.
     * @param departamento Objeto del departamento con los datos actualizados.
     * @return El departamento actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartamento(@PathVariable ObjectId id, @RequestBody DepartamentosModel departamento) {
        try {
            DepartamentosModel updatedDepartamento = departamentosService.updateDepartamento(id, departamento);
            return ResponseEntity.ok(updatedDepartamento);
        } catch (RecursoYaExistenteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch(RecursoNoEncontradoException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }   
}
