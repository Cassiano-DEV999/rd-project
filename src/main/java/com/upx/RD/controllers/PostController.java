package com.upx.RD.controllers;

import com.upx.RD.dto.ItemPostDto;
import com.upx.RD.dto.PostAgrupadoDto;
import com.upx.RD.model.Post;
import com.upx.RD.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping("/novo")
    public String exibirFormularioNovoPost() {
        return "criar-post";
    }


    @PostMapping("/criar")
    public String processarCriacaoPost(@RequestParam("titulo") String titulo, Principal principal) {

        Post novoPost = postService.criarPost(titulo, principal.getName());

        return "redirect:/posts/editar/" + novoPost.getId();
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicaoPost(@PathVariable("id") Long id, Model model, Principal principal) {
        try {
            Post post = postService.buscarPostParaEdicao(id, principal.getName());

            model.addAttribute("post", post);
            model.addAttribute("itemPostDto", new ItemPostDto());

            return "editar-post";
        } catch (Exception e) {
            return "redirect:/dashboard?erro=" + e.getMessage();
        }
    }

    @PostMapping("/{postId}/adicionar-item")
    public String processarAdicionarItem(
            @PathVariable("postId") Long postId,
            @Valid @ModelAttribute("itemPostDto") ItemPostDto dto,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erroItem", "Erro de validação. Preencha todos os campos.");
            return "redirect:/posts/editar/" + postId;
        }

        try {
            postService.adicionarItemAoPost(postId, dto, principal.getName());
            redirectAttributes.addFlashAttribute("sucessoItem", "Item adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroItem", "Erro: " + e.getMessage());
        }

        return "redirect:/posts/editar/" + postId;
    }

    @PostMapping("/gerar-agrupado")
    public String gerarPostAgrupado(
            @ModelAttribute PostAgrupadoDto postAgrupadoDto,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            Post postGerado = postService.gerarPostAgrupadoComPrecos(postAgrupadoDto, principal.getName());

            redirectAttributes.addFlashAttribute("sucessoItem",
                    "Post gerado com sucesso! Você pode continuar editando aqui.");

            return "redirect:/posts/editar/" + postGerado.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroPostObra", "Erro: " + e.getMessage());

            return "redirect:/dashboard";
        }
    }
}