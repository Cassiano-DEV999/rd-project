package com.upx.RD.dto;

import lombok.Data;

@Data
public class ItemVendaDto {
    private Long materialId;
    private String descricao;
    private double sobra;
    private String unidade;
    private double precoVenda;
}