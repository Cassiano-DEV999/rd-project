package com.upx.RD.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CadastroDto(

        @NotBlank(message = "O nome é obrigatório")
        String nomeCompleto,

        @Email(message = "Formato de email inválido")
        @NotBlank(message = "O email (login) é obrigatório")
        String username,

        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {

}