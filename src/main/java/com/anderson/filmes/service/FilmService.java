package com.anderson.filmes.service;

import com.anderson.filmes.exception.RecursoNaoEncontradoException;
import com.anderson.filmes.model.Film;
import com.anderson.filmes.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service            // declara como componente de serviço no contexto Spring
@RequiredArgsConstructor // Lombok: injeta FilmRepository via construtor
public class FilmService {

    private final FilmRepository filmRepository;
    // 'final' + @RequiredArgsConstructor = injeção de dependência automática

    public List<Film> listarAtivos() {
        return filmRepository.findByIsDeletedIsNull();
    }

    public List<Film> listarTodos() {
        return filmRepository.findAll();
    }

    public Film buscarPorId(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filme com ID " + id + " não encontrado!"));
    }

    public void salvar(Film film) {
        filmRepository.save(film);
    }

    public void deletar(Long id) {
        Film film = buscarPorId(id);
        film.setIsDeleted(LocalDate.now()); // soft delete
        filmRepository.save(film);
    }

    public void restaurar(Long id) {
        Film film = buscarPorId(id);
        film.setIsDeleted(null); // restaura
        filmRepository.save(film);
    }
}