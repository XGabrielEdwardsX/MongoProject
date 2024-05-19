package com.ProyectoMongo.api.Exception;

public class RecursoYaExistenteException extends RuntimeException {
    public RecursoYaExistenteException(String message) {
        super(message);
    }
}

