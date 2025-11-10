package com.upx.RD.controllers;

import com.upx.RD.dto.ItemVendaDto;
import com.upx.RD.dto.MaterialCadastroDto;
import com.upx.RD.dto.ObraCadastroDto;
import com.upx.RD.dto.PostAgrupadoDto;
import com.upx.RD.model.Material;
import com.upx.RD.model.Obra;
import com.upx.RD.services.MaterialService;
import com.upx.RD.services.ObraService;
import com.upx.RD.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;
    private final MaterialService materialService;
    private final PostService postService;


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

            PostAgrupadoDto postDto = new PostAgrupadoDto();
            postDto.setTituloPost(String.format("Sobra de Materiais - Obra '%s'", obra.getNomeObra()));
            postDto.setItens(new ArrayList<>());

            for (Material mat : materiais) {
                double sobra = mat.getQuantidadeSobraCalculada();
                if (sobra > 0) {
                    ItemVendaDto itemDto = new ItemVendaDto();
                    itemDto.setMaterialId(mat.getId());
                    itemDto.setDescricao(mat.getDescricao());
                    itemDto.setSobra(sobra);
                    itemDto.setUnidade(mat.getUnidade().name());
                    itemDto.setPrecoVenda(0.00);

                    postDto.getItens().add(itemDto);
                }
            }

            model.addAttribute("postAgrupadoDto", postDto);

            return "detalhe-obra";

        } catch (IllegalStateException e) {
            return "redirect:/dashboard?erro=" + e.getMessage();
        }
    }
}