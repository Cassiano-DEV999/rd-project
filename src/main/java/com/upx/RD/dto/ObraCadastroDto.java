package com.upx.RD.dto;

import jakarta.validation.constraints.NotBlank;


public record ObraCadastroDto(

        @NotBlank(message = "O nome da obra é obrigatório")
        String nomeObra,

        String endereco,

        String dataInicio
) {
}