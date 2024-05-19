package com.ProyectoMongo.api.Model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarjetasModel {
    private String tipo;
    private long numero;
    private Date fechaVencimiento;
}

