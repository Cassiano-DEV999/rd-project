package com.upx.RD.services;

import com.upx.RD.dto.ItemPostDto;
import com.upx.RD.dto.ItemVendaDto;
import com.upx.RD.dto.PostAgrupadoDto;
import com.upx.RD.model.*;
import com.upx.RD.repositorys.ItemPostRepository;
import com.upx.RD.repositorys.MaterialRepository;
import com.upx.RD.repositorys.PostRepository;
import com.upx.RD.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ItemPostRepository itemPostRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraService obraService;
    private final MaterialRepository materialRepository;


    @Transactional
    public Post criarPost(String titulo, String username) {
        Usuario proprietario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        Post novoPost = new Post();
        novoPost.setTitulo(titulo);
        novoPost.setProprietario(proprietario);

        return postRepository.save(novoPost);
    }


    @Transactional(readOnly = true)
    public Post buscarPostParaEdicao(Long postId, String username) {
        Post post = postRepository.findByIdWithItens(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        if (!post.getProprietario().getUsername().equals(username)) {
            throw new IllegalStateException("Acesso negado. Este post não pertence a você.");
        }

        return post;
    }


    @Transactional
    public void adicionarItemAoPost(Long postId, ItemPostDto dto, String username) {
        Post post = buscarPostParaEdicao(postId, username);

        ItemPost novoItem = new ItemPost();
        novoItem.setDescricao(dto.descricao());
        novoItem.setQuantidade(dto.quantidade());
        novoItem.setUnidade(dto.unidade());
        novoItem.setPreco(dto.preco());

        novoItem.setPost(post);

        itemPostRepository.save(novoItem);
    }

    @Transactional(readOnly = true)
    public List<Post> listarPostsDisponiveis() {
        return postRepository.findAllWithDetailsByStatus(StatusPost.DISPONIVEL);
    }

    @Transactional
    public Post gerarPostAgrupadoComPrecos(PostAgrupadoDto dto, String username) {

        Usuario proprietario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        Post novoPost = new Post();
        novoPost.setTitulo(dto.getTituloPost());
        novoPost.setProprietario(proprietario);
        novoPost.setItens(new ArrayList<>());

        for (ItemVendaDto itemDto : dto.getItens()) {

            if (itemDto.getPrecoVenda() > 0) {

                Material materialOrigem = materialRepository.findById(itemDto.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("Material não encontrado"));

                ItemPost novoItem = new ItemPost();
                novoItem.setDescricao(itemDto.getDescricao());
                novoItem.setQuantidade(itemDto.getSobra());
                novoItem.setUnidade(materialOrigem.getUnidade());
                novoItem.setPreco(itemDto.getPrecoVenda());

                novoItem.setPost(novoPost);
                novoPost.getItens().add(novoItem);
            }
        }

        if (novoPost.getItens().isEmpty()) {
            throw new IllegalStateException("Nenhum item com preço foi adicionado ao post.");
        }

        return postRepository.save(novoPost);
    }
}