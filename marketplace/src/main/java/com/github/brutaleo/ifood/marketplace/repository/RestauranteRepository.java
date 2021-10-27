package com.github.brutaleo.ifood.marketplace.repository;

import com.github.brutaleo.ifood.marketplace.model.Restaurante;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestauranteRepository implements PanacheRepositoryBase<Restaurante, Long> {

}
