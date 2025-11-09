package com.upx.RD.dto;

import lombok.Data;
import java.util.List;

@Data
public class PostAgrupadoDto {
    private String tituloPost;
    private List<ItemVendaDto> itens;
}