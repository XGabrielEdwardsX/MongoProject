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

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {
    @Autowired
    private IUsuariosService usuariosService;

    @GetMapping("/")
    public List<UsuariosModel> getAllUsuarios() {
        return usuariosService.findAllUsuarios();
    }

    @GetMapping("/{id}")
    public UsuariosModel getUsuarioById(@PathVariable String id) {
        return usuariosService.findUsuarioById(new ObjectId(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> createUsuario(@RequestBody UsuariosModel usuario) {
        try {
            UsuariosModel savedUsuario = usuariosService.saveUsuario(usuario);
            return ResponseEntity.ok(savedUsuario);
        } catch (RecursoYaExistenteException | ValorInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<UsuariosModel> updateUsuario(@PathVariable String id, @RequestBody UsuariosModel usuario) {
        UsuariosModel updatedUsuario = usuariosService.updateUsuario(new ObjectId(id), usuario);
        return ResponseEntity.ok(updatedUsuario);
    }
}
