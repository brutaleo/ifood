package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.model.Restaurante;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class RestauranteService {

    @Inject
    PgPool pgPool;

    @Incoming("restaurantes-out")
    public void recebeRestauranteELocalizacao(String json) {

        Jsonb create = JsonbBuilder.create();
        Restaurante restaurante = create.fromJson(json, Restaurante.class);
        restaurante.persist(pgPool);

        System.out.println("-----------------------------");
        System.out.println(json);
        System.out.println("-----------------------------");
        System.out.println(restaurante);

    }

}
