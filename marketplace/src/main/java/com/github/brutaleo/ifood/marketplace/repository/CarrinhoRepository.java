package com.github.brutaleo.ifood.marketplace.repository;

import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarrinhoRepository implements PanacheRepositoryBase<Carrinho, Long> {
}
