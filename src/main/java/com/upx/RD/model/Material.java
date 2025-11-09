package com.upx.RD.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição do material é obrigatória")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Unidade unidade;

    @Positive(message = "A quantidade deve ser positiva")
    private double quantidadeComprada;

    @PositiveOrZero(message = "O percentual não pode ser negativo")
    private double percentualDesperdicio;

    @PositiveOrZero(message = "O percentual não pode ser negativo")
    private double percentualSobra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @PositiveOrZero(message = "O preço não pode ser negativo")
    private double preco;


    public double getQuantidadeDesperdicioCalculado() {
        return this.quantidadeComprada * (this.percentualDesperdicio / 100.0);
    }

    public double getQuantidadeUtilizavel() {
        return this.quantidadeComprada - getQuantidadeDesperdicioCalculado();
    }

    public double getQuantidadeSobraCalculada() {
        return getQuantidadeUtilizavel() * (this.percentualSobra / 100.0);
    }

    public double getQuantidadeUsadaNaObra() {
        return getQuantidadeUtilizavel() - getQuantidadeSobraCalculada();
    }
}