package com.github.brutaleo.ifood.marketplace.dto;

import com.github.brutaleo.ifood.marketplace.model.Prato;

import java.util.Collection;

public class CarrinhoDTO {
    public Long id;

    public String cliente;

    private Collection<Prato> prato;

}
