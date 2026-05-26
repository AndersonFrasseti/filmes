package com.anderson.filmes.controller;

import com.anderson.filmes.model.Film;
import com.anderson.filmes.service.FilmService;
import com.anderson.filmes.dto.FilmForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller             // retorna nomes de views (não JSON)
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    // Imagens pré-existentes em /static/images/
    private static final List<String> IMAGENS = List.of(
            "/images/filme1.jpg",
            "/images/filme2.jpg",
            "/images/filme3.jpg"
    );

    // ===== QUESTÃO 4: /index =====
    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("filmes", filmService.listarAtivos());

        // conta itens no carrinho para exibir no link
        List<Film> carrinho = (List<Film>) session.getAttribute("carrinho");
        model.addAttribute("qtdCarrinho", carrinho != null ? carrinho.size() : 0);

        return "index"; // → templates/index.html
    }

    // ===== QUESTÃO 5: /admin =====
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("filmes", filmService.listarTodos());
        return "admin";
    }

    // ===== QUESTÃO 6: GET /cadastro — exibe formulário =====
    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute("filmForm", new FilmForm());
        return "cadastro";
    }

    // ===== QUESTÃO 7: GET /editar?id=X — exibe formulário preenchido =====
    @GetMapping("/editar")
    public String exibirEditar(@RequestParam Long id, Model model) {
        Film film = filmService.buscarPorId(id);
        // converte entidade → DTO para preencher o formulário
        FilmForm form = new FilmForm();
        form.setId(film.getId());
        form.setTitulo(film.getTitulo());
        form.setDiretor(film.getDiretor());
        form.setGenero(film.getGenero());
        form.setAnoLancamento(film.getAnoLancamento());
        form.setDuracao(film.getDuracao());
        form.setClassificacaoIndicativa(film.getClassificacaoIndicativa());
        form.setSinopse(film.getSinopse());
        model.addAttribute("filmForm", form);
        return "editar";
    }

    // ===== QUESTÃO 8: POST /salvar — salva ou atualiza =====
    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("filmForm") FilmForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // @Valid dispara as validações; BindingResult captura os erros
        if (result.hasErrors()) {
            // se há erros, volta ao formulário (não redireciona!)
            return form.getId() != null ? "editar" : "cadastro";
        }

        // seleciona imagem aleatória
        String imgAleatoria = IMAGENS.get(
                new Random().nextInt(IMAGENS.size())
        );

        Film film;
        if (form.getId() != null) {
            // edição: busca o existente para não perder isDeleted
            film = filmService.buscarPorId(form.getId());
        } else {
            // cadastro: cria novo
            film = new Film();
        }

        film.setTitulo(form.getTitulo());
        film.setDiretor(form.getDiretor());
        film.setGenero(form.getGenero());
        film.setAnoLancamento(form.getAnoLancamento());
        film.setDuracao(form.getDuracao());
        film.setClassificacaoIndicativa(form.getClassificacaoIndicativa());
        film.setSinopse(form.getSinopse());
        film.setImageUrl(imgAleatoria);

        filmService.salvar(film);

        // flash message — questão 19 (PRG Pattern)
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Filme salvo com sucesso!");
        return "redirect:/admin";
    }

    // ===== QUESTÃO 10: /deletar?id=X =====
    @GetMapping("/deletar")
    public String deletar(@RequestParam Long id,
                          RedirectAttributes redirectAttributes) {
        filmService.deletar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Filme removido (soft delete)!");
        return "redirect:/admin";
    }

    // ===== QUESTÃO 10: /restaurar?id=X =====
    @GetMapping("/restaurar")
    public String restaurar(@RequestParam Long id,
                            RedirectAttributes redirectAttributes) {
        filmService.restaurar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Filme restaurado com sucesso!");
        return "redirect:/admin";
    }

    // ===== QUESTÃO 9: /detalhe/{id} — PathVariable =====
    @GetMapping("/detalhe/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        // se não encontrar, FilmService lança RecursoNaoEncontradoException
        model.addAttribute("filme", filmService.buscarPorId(id));
        return "detalhe";
    }

    // ===== QUESTÃO 11: /adicionarCarrinho?id=X =====
    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam Long id,
                                    HttpSession session) {
        Film film = filmService.buscarPorId(id);

        // pega o carrinho da sessão ou cria um novo
        List<Film> carrinho = (List<Film>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        carrinho.add(film);
        session.setAttribute("carrinho", carrinho);

        return "redirect:/index";
    }

    // ===== QUESTÃO 12: /verCarrinho =====
    @GetMapping("/verCarrinho")
    public String verCarrinho(HttpSession session, Model model,
                              RedirectAttributes redirectAttributes) {
        List<Film> carrinho = (List<Film>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Não existem itens no carrinho!");
            return "redirect:/index";
        }
        model.addAttribute("carrinho", carrinho);
        return "carrinho";
    }

    // ===== QUESTÃO 13: /finalizarCompra =====
    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        session.invalidate(); // invalida toda a sessão HTTP
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Compra finalizada! Obrigado!");
        return "redirect:/index";
    }
}