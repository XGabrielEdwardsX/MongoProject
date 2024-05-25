package com.ProyectoMongo.api.Exception;

/**
 * La excepción StockInsuficienteException se lanza cuando se intenta realizar una operación
 * que requiere más stock del disponible. Esta excepción es una subclase de RuntimeException.
 */
public class StockInsuficienteException extends RuntimeException {
    
    /**
     * Construye una nueva excepción StockInsuficienteException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle.
     */
    public StockInsuficienteException(String message) {
        super(message);
    }
}
