package com.ProyectoMongo.api.Exception;

/**
 * La excepción RecursoYaExistenteException se lanza cuando se intenta crear un recurso que ya existe.
 * Esta excepción es una subclase de RuntimeException.
 */
public class RecursoYaExistenteException extends RuntimeException {
    
    /**
     * Construye una nueva excepción RecursoYaExistenteException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle.
     */
    public RecursoYaExistenteException(String message) {
        super(message);
    }
}
