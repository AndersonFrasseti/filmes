package com.anderson.filmes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity              // diz ao JPA: "esta classe vira uma tabela"
@Table(name = "films")
@Data               // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: construtor vazio (exigido pelo JPA)
@AllArgsConstructor // Lombok: construtor com todos os campos
@Builder            // Lombok: padrão Builder para criar objetos
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Id = chave primária; IDENTITY = auto-increment do Postgres
    private Long id;

    private LocalDate isDeleted;
    // NULL = ativo; com data = "deletado" (soft delete)

    private String imageUrl;
    // caminho da imagem em /static/images/

    @Column(nullable = false)
    private String titulo;

    private String diretor;

    private String genero;

    private Integer anoLancamento;

    private Integer duracao;
    // duração em minutos

    private String classificacaoIndicativa;
    // ex: "Livre", "10", "14", "18"

    @Column(length = 2000)
    private String sinopse;
}