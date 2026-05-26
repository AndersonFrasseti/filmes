package com.anderson.filmes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FilmForm {

    private Long id;
    // se id != null = edição; se id == null = novo cadastro

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Diretor é obrigatório")
    private String diretor;

    @NotBlank(message = "Gênero é obrigatório")
    private String genero;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1888, message = "Ano mínimo: 1888")
    @Max(value = 2030, message = "Ano máximo: 2030")
    private Integer anoLancamento;

    @NotNull(message = "Duração é obrigatória")
    private Integer duracao;

    // @Pattern com Regex — exigência da questão 3!
    // Classificação deve ser: Livre, 10, 12, 14, 16 ou 18
    @NotBlank(message = "Classificação é obrigatória")
    @Pattern(
            regexp = "^(Livre|10|12|14|16|18)$",
            message = "Classificação deve ser: Livre, 10, 12, 14, 16 ou 18"
    )
    private String classificacaoIndicativa;

    @NotBlank(message = "Sinopse é obrigatória")
    private String sinopse;
}