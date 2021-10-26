package com.github.brutaleo.ifood.marketplace.repository;

import com.github.brutaleo.ifood.marketplace.model.Prato;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PratoRepository implements PanacheRepositoryBase<Prato, Long> {

}
