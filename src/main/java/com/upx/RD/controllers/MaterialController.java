package com.upx.RD.controllers;

import com.upx.RD.dto.MaterialCadastroDto;
import com.upx.RD.services.MaterialService;
import com.upx.RD.services.ObraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;
    private final ObraService obraService;


    @PostMapping("/obras/{obraId}/materiais/criar")
    public String criarNovoMaterial(
            @PathVariable("obraId") Long obraId,
            @Valid @ModelAttribute("novoMaterialDto") MaterialCadastroDto dto,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            obraService.buscarObraPorIdEUsuario(obraId, principal.getName());

        } catch (IllegalStateException e) {

            return "redirect:/dashboard?erroAcesso";
        }


        if (result.hasErrors()) {

            redirectAttributes.addFlashAttribute("erroMaterial", "Dados inválidos. Verifique o formulário.");

            return "redirect:/obras/" + obraId;
        }

        materialService.adicionarMaterial(dto, obraId);

        redirectAttributes.addFlashAttribute("sucessoMaterial", "Material adicionado com sucesso!");

        return "redirect:/obras/" + obraId;
    }
}