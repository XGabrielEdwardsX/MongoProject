package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
//import java.sql.Date;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentariosModel {
    private ObjectId idUsuario;
    private String texto;
    private Date fecha;
}