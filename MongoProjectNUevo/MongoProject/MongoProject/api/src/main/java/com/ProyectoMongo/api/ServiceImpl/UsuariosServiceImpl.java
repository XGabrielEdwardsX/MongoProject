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

import java.util.List;
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

        for (RolesModel rolModel : usuario.getRoles()) {
            try {
                Rol.valueOf(rolModel.getRol()); // Validar rol
            } catch (IllegalArgumentException e) {
                throw new ValorInvalidoException("Valor inválido para el rol: " + rolModel.getRol());
            }

            if (usuarioRepository.existsByNombreUsuario(rolModel.getNombreUsuario())) {
                throw new RecursoYaExistenteException("El nombre de usuario " + rolModel.getNombreUsuario() + " ya está en uso.");
            }
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
        UsuariosModel existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID: " + id + " no encontrado"));

        if (!existingUsuario.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RecursoYaExistenteException("El correo electrónico ya está en uso.");
        }
        if (!existingUsuario.getTelefono().equals(usuario.getTelefono()) && usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new RecursoYaExistenteException("El teléfono ya está en uso.");
        }

        try {
            Genero.valueOf(usuario.getGenero());
        } catch (IllegalArgumentException e) {
            throw new ValorInvalidoException("Valor inválido para el género: " + usuario.getGenero());
        }

        for (RolesModel rolModel : usuario.getRoles()) {
            try {
                Rol.valueOf(rolModel.getRol()); // Validar rol
            } catch (IllegalArgumentException e) {
                throw new ValorInvalidoException("Valor inválido para el rol: " + rolModel.getRol());
            }

            if (!existingUsuario.getRoles().contains(rolModel) && usuarioRepository.existsByNombreUsuario(rolModel.getNombreUsuario())) {
                throw new RecursoYaExistenteException("El nombre de usuario " + rolModel.getNombreUsuario() + " ya está en uso.");
            }
        }

        return usuarioRepository.save(usuario);
    }
}
