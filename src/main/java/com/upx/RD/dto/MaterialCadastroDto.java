package com.upx.RD.dto;

import com.upx.RD.model.Unidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialCadastroDto {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Unidade é obrigatória")
    private Unidade unidade;

    @PositiveOrZero(message = "O preço não pode ser negativo")
    private double preco;

    @Positive(message = "A quantidade deve ser positiva")
    private double quantidadeComprada;

    @PositiveOrZero(message = "Percentual de desperdício não pode ser negativo")
    private double percentualDesperdicio;

    @PositiveOrZero(message = "Percentual de sobra não pode ser negativo")
    private double percentualSobra;
}