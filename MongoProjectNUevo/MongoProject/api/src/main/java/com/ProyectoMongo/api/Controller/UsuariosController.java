package com.ProyectoMongo.api.Controller;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.UsuariosModel;
import com.ProyectoMongo.api.Service.IUsuariosService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {
    
    @Autowired
    private IUsuariosService usuariosService;

    /**
     * Obtener todos los usuarios.
     * @return Lista de todos los usuarios.
     */
    @GetMapping("/")
    public List<UsuariosModel> getAllUsuarios() {
        return usuariosService.findAllUsuarios();
    }

    /**
     * Obtener un usuario por su ID.
     * @param id ID del usuario.
     * @return El usuario correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String id) {
        try {
            UsuariosModel usuario = usuariosService.findUsuarioById(new ObjectId(id));
            return ResponseEntity.ok(usuario);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Crear un nuevo usuario.
     * @param usuario Objeto del usuario a crear.
     * @return El usuario creado.
     */
    @PostMapping("/")
    public ResponseEntity<?> createUsuario(@RequestBody UsuariosModel usuario) {
        try {
            UsuariosModel savedUsuario = usuariosService.saveUsuario(usuario);
            return ResponseEntity.ok(savedUsuario);
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (RecursoNoEncontradoException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Eliminar un usuario por su ID.
     * @param id ID del usuario a eliminar.
     * @return El usuario eliminado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        UsuariosModel deletedUsuario = usuariosService.deleteUsuario(objectId);
        if (deletedUsuario != null) {
            return ResponseEntity.ok(deletedUsuario);
        } else {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
    }

    /**
     * Actualizar un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param usuario Objeto del usuario con los datos actualizados.
     * @return El usuario actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable ObjectId id, @RequestBody UsuariosModel usuario) {
        try {
            UsuariosModel updatedUsuario = usuariosService.updateUsuario(id, usuario);
            return ResponseEntity.ok(updatedUsuario);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}
