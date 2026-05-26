package com.anderson.filmes.repository;

import com.anderson.filmes.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    // Spring Data gera o SQL automaticamente a partir do nome do método!

    // SELECT * FROM films WHERE is_deleted IS NULL
    List<Film> findByIsDeletedIsNull();

    // SELECT * FROM films (todos, inclusive deletados) — para /admin
    // findAll() já existe no JpaRepository
}
