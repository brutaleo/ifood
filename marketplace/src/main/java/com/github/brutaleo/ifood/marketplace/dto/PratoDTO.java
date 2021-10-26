package com.github.brutaleo.ifood.marketplace.dto;

import java.math.BigDecimal;

public class PratoDTO {
    public Long id;

    public String nome;

    public String descricao;

    public BigDecimal preco;

    public RestauranteDTO restaurante;
}
