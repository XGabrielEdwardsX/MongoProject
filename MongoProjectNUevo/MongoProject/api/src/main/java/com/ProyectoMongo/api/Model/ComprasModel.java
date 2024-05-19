package com.ProyectoMongo.api.Model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "compra")
public class ComprasModel {
    @Id
    private ObjectId id;

    private ObjectId idUsuario;
    private List<DetallesCompraModel> detallesCompra;
    private TarjetasModel tarjeta;
    private String estado;
    private String descripcion;
    private Date fechaCompra;
    private DestinatariosModel destinatario;
}
