package com.ProyectoMongo.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinatariosModel {
    private String nombre;
    private String apellido;
    private long codigoPostalCiudad;
    private String direccion;
    private String detallesDireccion;
    private long telefono;
}
