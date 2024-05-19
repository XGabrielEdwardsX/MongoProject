package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Enum.Genero;
import com.ProyectoMongo.api.Enum.Rol;
import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.RolesModel;
import com.ProyectoMongo.api.Model.UsuariosModel;
import com.ProyectoMongo.api.Repository.IUsuariosRepository;
import com.ProyectoMongo.api.Service.IUsuariosService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
public class UsuariosServiceImpl implements IUsuariosService {

    @Autowired
    IUsuariosRepository usuarioRepository;

    @Override
    public List<UsuariosModel> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public UsuariosModel findUsuarioById(ObjectId id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("usuario con ID: " + id + " no encontrado"));
    }

    @Override
    public UsuariosModel saveUsuario(UsuariosModel usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RecursoYaExistenteException("El correo electrónico ya está en uso.");
        }
        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new RecursoYaExistenteException("El teléfono ya está en uso.");
        }

        try {
            Genero.valueOf(usuario.getGenero());
        } catch (IllegalArgumentException e) {
            throw new ValorInvalidoException("Valor inválido para el género: " + usuario.getGenero());
        }

        Set<String> nombresUsuarios = new HashSet<>();
        for (RolesModel rolModel : usuario.getRoles()) {
            try {
                Rol.valueOf(rolModel.getRol()); // Validar rol
            } catch (IllegalArgumentException e) {
                throw new ValorInvalidoException("Valor inválido para el rol: " + rolModel.getRol());
            }

            if (usuarioRepository.existsByRolesNombreUsuario(rolModel.getNombreUsuario())) {
                throw new RecursoYaExistenteException("El nombre de usuario " + rolModel.getNombreUsuario() + " ya está en uso.");
            }


            nombresUsuarios.add(rolModel.getNombreUsuario());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuariosModel deleteUsuario(ObjectId id) {
        Optional<UsuariosModel> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return usuario.get();
        }
        throw new RecursoNoEncontradoException("Usuario con ID: " + id + " No encontrado");
    }

    @Override
    public UsuariosModel updateUsuario(ObjectId id, UsuariosModel usuario) {
        // Find the existing user by ID
        UsuariosModel existingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        // Check if the user trying to update is the same as the existing user
        if (!existingUsuario.getId().equals(usuario.getId())) {
            throw new RecursoNoEncontradoException("El id no coincide");
        }

        // Validate new email and phone number (excluding current user)
        boolean updateEmail = !existingUsuario.getEmail().equals(usuario.getEmail());
        boolean updatePhone = !existingUsuario.getTelefono().equals(usuario.getTelefono());

        if (updateEmail && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RecursoYaExistenteException("El correo electrónico ya está en uso.");
        }
        if (updatePhone && usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new RecursoYaExistenteException("El teléfono ya está en uso.");
        }

        Set<String> nombresUsuarios = new HashSet<>();
        List<RolesModel> NombreUsuarioUpdate = new ArrayList<>();
        for (RolesModel rol : usuario.getRoles()) {

            if (usuarioRepository.existsByRolesNombreUsuarioAndIdNot(rol.getNombreUsuario(), id)) {
                throw new RecursoYaExistenteException("El nombre de usuario " + rol.getNombreUsuario() + " ya lo tiene otro usuario");
            }
                
            existingUsuario.setNombre(usuario.getNombre());
            existingUsuario.setEmail(updateEmail ? usuario.getEmail() : existingUsuario.getEmail());
            existingUsuario.setTelefono(updatePhone ? usuario.getTelefono() : existingUsuario.getTelefono());
            existingUsuario.setEdad(usuario.getEdad());
            existingUsuario.setGenero(usuario.getGenero());
            existingUsuario.setDirecciones(usuario.getDirecciones());
            existingUsuario.setRoles(usuario.getRoles());

            try {
                Genero.valueOf(usuario.getGenero());
            } catch (IllegalArgumentException e) {
                throw new ValorInvalidoException("Valor inválido para el género: " + usuario.getGenero());
            }
            NombreUsuarioUpdate.add(rol);
            nombresUsuarios.add(rol.getNombreUsuario());
        }

        existingUsuario.setRoles(NombreUsuarioUpdate);
        return usuarioRepository.save(existingUsuario);
    }
}