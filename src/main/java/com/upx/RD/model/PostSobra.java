package com.upx.RD.model; // Seu pacote está correto

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts_sobra")
public class PostSobra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título do post é obrigatório")
    private String titulo;

    @PositiveOrZero(message = "O preço não pode ser negativo")
    private double precoTotal;

    @Enumerated(EnumType.STRING)
    private StatusPost status;

    private LocalDate dataCriacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id", unique = true)
    private Obra obraOrigem;


    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDate.now();
        this.status = StatusPost.DISPONIVEL;
    }
}