package com.upx.RD.services; // Mantenha seu pacote

import com.upx.RD.model.Obra;
import com.upx.RD.model.PostSobra;
import com.upx.RD.model.StatusPost;
import com.upx.RD.repositorys.ObraRepository;
import com.upx.RD.repositorys.PostSobraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSobraService {

    private final ObraRepository obraRepository;
    private final PostSobraRepository postSobraRepository;

    @Transactional
    public void criarPostSobraAgrupado(Long obraId, double precoTotal, String username) {


        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada."));


        if (!obra.getProprietario().getUsername().equals(username)) {
            throw new IllegalStateException("Acesso negado. Você não é o proprietário desta obra.");
        }


        if (obra.getPostDeSobra() != null) {
            throw new IllegalStateException("Um post de sobras para esta obra já existe.");
        }


        if (obra.getMateriais() == null || obra.getMateriais().isEmpty()) {
            throw new IllegalStateException("Não é possível postar. Adicione materiais à obra primeiro.");
        }

        String nomeObra = obra.getNomeObra();
        String tituloPost = String.format("Sobra de Materiais Agrupados - Obra '%s'", nomeObra);

        PostSobra novoPost = new PostSobra();
        novoPost.setTitulo(tituloPost);
        novoPost.setPrecoTotal(precoTotal);


        novoPost.setObraOrigem(obra);

        obra.setPostDeSobra(novoPost);


        obraRepository.save(obra);
    }

    @Transactional(readOnly = true)
    public List<PostSobra> listarPostsDisponiveis() {
        return postSobraRepository.findByStatusWithDetails(StatusPost.DISPONIVEL);
    }

}