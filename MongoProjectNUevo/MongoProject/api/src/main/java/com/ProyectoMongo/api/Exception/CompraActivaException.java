package com.ProyectoMongo.api.Exception;

/**
 * La excepción CompraActivaException se lanza cuando se intenta realizar una operación
 * no permitida debido a que existe una compra activa. Esta excepción es una subclase de RuntimeException.
 */
public class CompraActivaException extends RuntimeException {
    
    /**
     * Construye una nueva excepción CompraActivaException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle.
     */
    public CompraActivaException(String message) {
        super(message);
    }
}
