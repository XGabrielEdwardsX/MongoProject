package com.ProyectoMongo.api.Model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una tarjeta de crédito o débito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarjetasModel {
    /**
     * Tipo de la tarjeta (crédito o débito).
     */
    private String tipo;

    /**
     * Número de la tarjeta.
     */
    private long numero;

    /**
     * Fecha de vencimiento de la tarjeta.
     */
    private Date fechaVencimiento;
}
