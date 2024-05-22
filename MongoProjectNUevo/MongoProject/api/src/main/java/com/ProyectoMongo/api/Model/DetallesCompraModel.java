package com.ProyectoMongo.api.Model;

import java.math.BigDecimal;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallesCompraModel { 
    
    private String tipo;
    private ObjectId idTipo;
    private long cantidad;
    private String talla;
    private String color;
    private String imagenPersonalizada;
    private String detalles;
    private BigDecimal precio;
}

