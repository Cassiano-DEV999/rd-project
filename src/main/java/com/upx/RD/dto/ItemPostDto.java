package com.upx.RD.dto;

import com.upx.RD.model.Unidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPostDto(
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Positive(message = "Quantidade deve ser positiva")
        double quantidade,

        @NotNull(message = "Unidade é obrigatória")
        Unidade unidade,

        @Positive(message = "Preço deve ser positivo")
        double preco
) {
    public ItemPostDto() {
        this("", 0.0, null, 0.0);
    }
}