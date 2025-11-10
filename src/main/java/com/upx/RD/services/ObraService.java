package com.upx.RD.services;

import com.upx.RD.dto.ObraCadastroDto;
import com.upx.RD.model.Obra;
import com.upx.RD.model.Usuario;
import com.upx.RD.repositorys.ObraRepository;
import com.upx.RD.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObraService {

    private final ObraRepository obraRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Obra> listarObrasPorUsuario(String username) {
        Long usuarioId = getUsuarioLogado(username).getId();
        return obraRepository.findByProprietarioId(usuarioId);
    }


    @Transactional
    public void criarNovaObra(ObraCadastroDto dto, String username) {

        Usuario proprietario = getUsuarioLogado(username);


        Obra novaObra = new Obra();
        novaObra.setNomeObra(dto.nomeObra());
        novaObra.setEndereco(dto.endereco());


        if (dto.dataInicio() != null && !dto.dataInicio().isBlank()) {
            novaObra.setDataInicio(LocalDate.parse(dto.dataInicio(), DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            novaObra.setDataInicio(LocalDate.now());
        }


        novaObra.setProprietario(proprietario);

        obraRepository.save(novaObra);
    }

    private Usuario getUsuarioLogado(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado no banco de dados."));
    }

    @Transactional(readOnly = true)
    public Obra buscarObraPorIdEUsuario(Long obraId, String username) {

        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new IllegalStateException("Obra não encontrada. ID: " + obraId));

        if (!obra.getProprietario().getUsername().equals(username)) {

            throw new IllegalStateException("Acesso negado. Esta obra não pertence a você.");
        }

        return obra;
    }
}