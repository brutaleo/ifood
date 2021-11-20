package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PratoMapper;
import com.github.brutaleo.ifood.marketplace.repository.PratoRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PratoService {
    @Inject
    PratoRepository pratoRepository;
    @Inject
    PratoMapper pratoMapper;

    public Uni<List<PratoDTO>> findAll() {
        return pratoRepository.listAll().map(pratoMapper::toDTOList);
    }

    public Uni<List<PratoDTO>> findByRestaurante(Long restaurante_id) {
        return pratoRepository
                .list("#Prato.getByRestauranteId", restaurante_id)
                .map(pratoMapper::toDTOList);
    }

    public Uni<PratoDTO> findById(PgPool client, Long prato_id) {
        return client
                .preparedQuery("SELECT * FROM prato WHERE id = $1")
                .execute(
                        Tuple.of(prato_id)
                ).map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? PratoDTO.from(iterator.next()) : null);

    }


}
