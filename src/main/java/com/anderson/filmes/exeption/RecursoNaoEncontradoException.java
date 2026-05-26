package com.anderson.filmes.exeption;

// RuntimeException = não precisa ser declarada no throws
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}