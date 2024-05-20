package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Enum.Genero;
import com.ProyectoMongo.api.Enum.Rol;
import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.DireccionesModel;
import com.ProyectoMongo.api.Model.RolesModel;
import com.ProyectoMongo.api.Model.UsuariosModel;
import com.ProyectoMongo.api.Repository.IDepartamentosRepository;
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
    @Autowired
    IDepartamentosRepository departamentosRepository;

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

            Set<Integer> codigosPostales = new HashSet<>();
            departamentosRepository.findAll().forEach(departamento -> 
            departamento.getCiudades().forEach(ciudad -> codigosPostales.add((int) ciudad.getCodigoPostal()))
        );

        for (DireccionesModel direccion : usuario.getDirecciones()) {
            if (!codigosPostales.contains(direccion.getCodigoPostalCiudad())) {
                throw new RecursoNoEncontradoException("Código postal " + direccion.getCodigoPostalCiudad() + " no encontrado");
            }
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
    UsuariosModel existingUsuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID: " + id + " no encontrado"));

    // Actualizar solo los campos presentes en la solicitud
    if (usuario.getNombre() != null) {
        existingUsuario.setNombre(usuario.getNombre());
    }
    if (usuario.getEmail() != null) {
        if (usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), id)) {
            throw new RecursoYaExistenteException("El correo electrónico " + usuario.getEmail() + " ya está en uso.");
        }
        existingUsuario.setEmail(usuario.getEmail());
    }
    if (usuario.getTelefono() != null) {
        if (usuarioRepository.existsByTelefonoAndIdNot(usuario.getTelefono(), id)) {
            throw new RecursoYaExistenteException("El teléfono " + usuario.getTelefono() + " ya está en uso.");
        }
        existingUsuario.setTelefono(usuario.getTelefono());
    }
    if (usuario.getEdad() != null) {
        existingUsuario.setEdad(usuario.getEdad());
    }
    if (usuario.getGenero() != null) {
        try {
            Genero.valueOf(usuario.getGenero());
        } catch (IllegalArgumentException e) {
            throw new ValorInvalidoException("Valor inválido para el género: " + usuario.getGenero());
        }
        existingUsuario.setGenero(usuario.getGenero());
    }
    if (usuario.getDirecciones() != null && !usuario.getDirecciones().isEmpty()) {
        List<DireccionesModel> direccionesActualizadas = new ArrayList<>(existingUsuario.getDirecciones());
        for (DireccionesModel nuevaDireccion : usuario.getDirecciones()) {
            direccionesActualizadas.add(nuevaDireccion);
        }
        existingUsuario.setDirecciones(direccionesActualizadas);
    }
    if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
        Set<String> nombresUsuarios = new HashSet<>();
        List<RolesModel> rolesActualizados = new ArrayList<>(existingUsuario.getRoles());
        for (RolesModel rol : usuario.getRoles()) {
            if (usuarioRepository.existsByRolesNombreUsuarioAndIdNot(rol.getNombreUsuario(), id)) {
                throw new RecursoYaExistenteException("El nombre de usuario " + rol.getNombreUsuario() + " ya lo tiene otro usuario");
            }
            rolesActualizados.add(rol);
            nombresUsuarios.add(rol.getNombreUsuario());
        }
        existingUsuario.setRoles(rolesActualizados);
    }

    return usuarioRepository.save(existingUsuario);
}

}
   