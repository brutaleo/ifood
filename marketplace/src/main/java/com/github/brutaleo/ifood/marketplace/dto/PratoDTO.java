package com.github.brutaleo.ifood.marketplace.dto;

import io.vertx.mutiny.sqlclient.Row;

import java.math.BigDecimal;

public class PratoDTO {
    public Long id;

    public String nome;

    public String descricao;

    public BigDecimal preco;

    public RestauranteDTO restaurante;

    public static PratoDTO from(Row row) {
        PratoDTO dto = new PratoDTO();
        dto.descricao = row.getString("descricao");
        dto.nome = row.getString("nome");
        dto.preco = row.getBigDecimal("preco");
        dto.id = row.getLong("id");

        return dto;
    }
}
