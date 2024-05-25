package com.ProyectoMongo.api.Enum;

/**
 * El enumerado Tarjeta representa los tipos de tarjetas posibles que pueden ser
 * utilizadas en la aplicación. Este enumerado se utiliza para mantener consistencia
 * en los valores de tipos de tarjeta y evitar errores de validación o de entrada de datos incorrectos.
 */
public enum Tarjeta {
    /**
     * Representa la tarjeta Maestro.
     */
    MAESTRO,

    /**
     * Representa la tarjeta Visa.
     */
    VISA,

    /**
     * Representa la tarjeta MasterCard.
     */
    MASTERCARD;
}
