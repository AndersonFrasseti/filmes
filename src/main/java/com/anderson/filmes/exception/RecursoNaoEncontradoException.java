package com.anderson.filmes.exception;

// RuntimeException = não precisa ser declarada no throws
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}