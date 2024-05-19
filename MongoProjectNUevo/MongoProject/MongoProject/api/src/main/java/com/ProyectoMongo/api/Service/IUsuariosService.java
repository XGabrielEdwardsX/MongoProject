package com.ProyectoMongo.api.Service;


import com.ProyectoMongo.api.Model.UsuariosModel;
import org.bson.types.ObjectId;
import java.util.List;

public interface IUsuariosService {

    List<UsuariosModel> findAllUsuarios();
    UsuariosModel findUsuarioById(ObjectId id);
    UsuariosModel saveUsuario(UsuariosModel usuario);
    UsuariosModel deleteUsuario(ObjectId id);
    UsuariosModel updateUsuario(ObjectId id, UsuariosModel usuario);
}