package com.upx.RD.controllers;

import com.upx.RD.dto.CadastroDto;
import com.upx.RD.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String exibirPaginaLogin(Model model) {
        return "login";
    }


    @GetMapping("/cadastro")
    public String exibirPaginaCadastro(Model model) {
        model.addAttribute("usuarioDto", new CadastroDto("", "", ""));
        return "cadastro";
    }


    @PostMapping("/cadastro")
    public String processarCadastro(
            @Valid @ModelAttribute("usuarioDto") CadastroDto usuarioDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "cadastro";
        }

        try {
            usuarioService.cadastrarNovoUsuario(usuarioDto);

            return "redirect:/login?cadastroSucesso";

        } catch (IllegalStateException e) {
            model.addAttribute("erroCadastro", e.getMessage());
            return "cadastro";
        }
    }
}