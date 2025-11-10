package com.upx.RD.services;

import com.upx.RD.dto.MaterialCadastroDto;
import com.upx.RD.model.Material;
import com.upx.RD.model.Obra;

import com.upx.RD.repositorys.MaterialRepository;
import com.upx.RD.repositorys.ObraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ObraRepository obraRepository;


    @Transactional(readOnly = true)
    public List<Material> listarMateriaisPorObra(Long obraId) {
        return materialRepository.findByObraId(obraId);
    }


    @Transactional
    public void adicionarMaterial(MaterialCadastroDto dto, Long obraId) {
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));

        Material novoMaterial = new Material();
        novoMaterial.setDescricao(dto.getDescricao());
        novoMaterial.setUnidade(dto.getUnidade());
        novoMaterial.setQuantidadeComprada(dto.getQuantidadeComprada());

        novoMaterial.setPreco(dto.getPreco());


        novoMaterial.setPercentualDesperdicio(dto.getPercentualDesperdicio());
        novoMaterial.setPercentualSobra(dto.getPercentualSobra());
        novoMaterial.setObra(obra);

        materialRepository.save(novoMaterial);
    }

}