package com.ProyectoMongo.api.Exception;

/**
 * La excepción RecursoNoEncontradoException se lanza cuando un recurso solicitado no se encuentra.
 * Esta excepción es una subclase de RuntimeException.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    
    /**
     * Construye una nueva excepción RecursoNoEncontradoException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle.
     */
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
