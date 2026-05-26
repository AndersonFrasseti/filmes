package com.anderson.filmes.advice;

import com.anderson.filmes.exception.RecursoNaoEncontradoException;import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// "para-raios" — intercepta exceções de TODOS os controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    // captura especificamente essa exceção antes que chegue ao Tomcat
    public String handleNaoEncontrado(
            RecursoNaoEncontradoException ex,
            Model model) {
        // injeta a mensagem na view — sem expor stack trace!
        model.addAttribute("mensagemErro", ex.getMessage());
        return "erro"; // → templates/erro.html
    }

    @ExceptionHandler(Exception.class)
    // captura qualquer outra exceção inesperada
    public String handleGenerico(Exception ex, Model model) {
        model.addAttribute("mensagemErro", "Ocorreu um erro inesperado.");
        return "erro";
    }
}