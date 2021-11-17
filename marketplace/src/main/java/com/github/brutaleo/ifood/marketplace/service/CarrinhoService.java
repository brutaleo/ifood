package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.CarrinhoDTO;
import com.github.brutaleo.ifood.marketplace.dto.CarrinhoMapper;
import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import com.github.brutaleo.ifood.marketplace.repository.CarrinhoRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CarrinhoService {
    @Inject
    CarrinhoRepository carrinhoRepository;
    @Inject
    CarrinhoMapper carrinhoMapper;

    public Uni<List<CarrinhoDTO>> findByCliente(String cliente) {
        return carrinhoRepository
                .list("#Carrinho.getByCliente", cliente)
                .map(carrinhoMapper::toDTOList);
    }

    public Uni<CarrinhoDTO> findById(Long carrino_id) {
        return carrinhoRepository
                .findById(carrino_id)
                .map(carrinhoMapper::toDTO);
    }

    public Uni<String> salvarPratoNoCarrinho(PgPool client, String cliente, Long prato) {
        return client
                .preparedQuery(
                        "INSERT INTO carrinho (cliente, prato) VALUES ($1, $2) RETURNING (cliente)")
                .execute(
                        Tuple.of(cliente, prato)
                ).map(
                        pgRowSet -> pgRowSet
                        .iterator()
                        .next()
                        .getString("cliente")
                );
    }

    public Uni<List<Carrinho>> buscarCarrinho(PgPool client, String cliente) {
        return client
                .preparedQuery("SELECT * FROM carrinho WHERE cliente = $1 ")
                .execute(
                        Tuple.of(cliente)
                ).map(
                        pgRowSet -> {
                            List<Carrinho> list = new ArrayList<>(pgRowSet.size());
                    for (Row row : pgRowSet) {
                        list.add(carrinhoMapper.toPratoCarrinho(row));
                    }
                    return list;
                });
    }

    public Uni<Boolean> deletarCarrinho(PgPool client, String cliente) {
        return client
                .preparedQuery("DELETE FROM carrinho WHERE cliente = $1")
                .execute(
                        Tuple.of(cliente)
                ).map(
                        pgRowSet -> pgRowSet.rowCount() == 1
                );
    }

}
