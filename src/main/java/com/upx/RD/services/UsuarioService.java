package com.upx.RD.services;

import com.upx.RD.dto.CadastroDto;
import com.upx.RD.model.Role;
import com.upx.RD.model.Usuario;
import com.upx.RD.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Marca esta classe como um "Serviço" do Spring
@RequiredArgsConstructor // Injeta as dependências via construtor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Injetado do SecurityConfig


    public void cadastrarNovoUsuario(CadastroDto dto) {

        if (usuarioRepository.existsByUsername(dto.username())) {

            throw new IllegalStateException("Erro: Este email (username) já está em uso.");
        }


        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(dto.nomeCompleto());
        novoUsuario.setUsername(dto.username());


        novoUsuario.setPassword(passwordEncoder.encode(dto.password()));


        novoUsuario.setRole(Role.ROLE_USER);


        usuarioRepository.save(novoUsuario);
    }
}