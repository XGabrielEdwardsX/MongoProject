package com.ProyectoMongo.api.Exception;

/**
 * La excepción ValorInvalidoException se lanza cuando se proporciona un valor inválido para una operación o entidad.
 * Esta excepción es una subclase de RuntimeException.
 */
public class ValorInvalidoException extends RuntimeException {
    
    /**
     * Construye una nueva excepción ValorInvalidoException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle.
     */
    public ValorInvalidoException(String message) {
        super(message);
    }
}
