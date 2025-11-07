package com.upx.RD.controllers;

import com.upx.RD.dto.MaterialCadastroDto;
import com.upx.RD.dto.ObraCadastroDto;
import com.upx.RD.model.Material;
import com.upx.RD.model.Obra;
import com.upx.RD.services.MaterialService;
import com.upx.RD.services.ObraService;
import com.upx.RD.services.PostSobraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;
    private final MaterialService materialService;
    private final PostSobraService postSobraService;


    @GetMapping("/dashboard")
    public String exibirDashboard(Model model, Principal principal) {

        String username = principal.getName();

        List<Obra> obrasDoUsuario = obraService.listarObrasPorUsuario(username);

        model.addAttribute("obras", obrasDoUsuario);

        model.addAttribute("novaObraDto", new ObraCadastroDto("", "", ""));

        model.addAttribute("nomeUsuario", username);


        return "dashboard";
    }


    @PostMapping("/obras/criar")
    public String criarNovaObra(
            @Valid @ModelAttribute("novaObraDto") ObraCadastroDto dto,
            BindingResult result,
            Principal principal,
            Model model) {


        String username = principal.getName();


        if (result.hasErrors()) {

            List<Obra> obrasDoUsuario = obraService.listarObrasPorUsuario(username);
            model.addAttribute("obras", obrasDoUsuario);
            model.addAttribute("nomeUsuario", username);

            return "dashboard";
        }

        try {

            obraService.criarNovaObra(dto, username);


            return "redirect:/dashboard";

        } catch (Exception e) {

            List<Obra> obrasDoUsuario = obraService.listarObrasPorUsuario(username);
            model.addAttribute("obras", obrasDoUsuario);
            model.addAttribute("nomeUsuario", username);
            model.addAttribute("erroGlobal", "Erro ao salvar a obra: " + e.getMessage());

            return "dashboard";
        }
    }

    @GetMapping("/obras/{id}")
    public String exibirDetalhesDaObra(
            @PathVariable("id") Long id,
            Model model,
            Principal principal) {

        try {
            Obra obra = obraService.buscarObraPorIdEUsuario(id, principal.getName());

            List<Material> materiais = materialService.listarMateriaisPorObra(id);

            model.addAttribute("obra", obra);
            model.addAttribute("materiais", materiais);

            model.addAttribute("novoMaterialDto", new MaterialCadastroDto());

            return "detalhe-obra";

        } catch (IllegalStateException e) {
            return "redirect:/dashboard?erro=" + e.getMessage();
        }
    }

    @PostMapping("/obras/{id}/criar-post")
    public String criarPostAgrupado(
            @PathVariable("id") Long obraId,
            @RequestParam("precoTotal") double precoTotal,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String redirectUrl = "redirect:/obras/" + obraId;

        try {
            String username = principal.getName();

            postSobraService.criarPostSobraAgrupado(obraId, precoTotal, username);

            redirectAttributes.addFlashAttribute("sucessoPost", "Post agrupado criado com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroPost", "Erro ao criar post: " + e.getMessage());
        }

        return redirectUrl;
    }
}