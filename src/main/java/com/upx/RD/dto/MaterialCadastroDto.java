package com.upx.RD.dto;

import com.upx.RD.model.Unidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record MaterialCadastroDto(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Unidade é obrigatória")
        Unidade unidade,

        @Positive(message = "Quantidade deve ser positiva")
        double quantidadeComprada,

        @PositiveOrZero(message = "Percentual de desperdício não pode ser negativo")
        double percentualDesperdicio, // Ex: 30%

        @PositiveOrZero(message = "Percentual de sobra não pode ser negativo")
        double percentualSobra // Ex: 20%


) {

    public MaterialCadastroDto() {
        this("", null, 0.0, 0.0, 0.0);
    }
}