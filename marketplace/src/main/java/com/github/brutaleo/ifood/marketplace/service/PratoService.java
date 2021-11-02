package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PratoMapper;
import com.github.brutaleo.ifood.marketplace.repository.PratoRepository;
import io.smallrye.mutiny.Uni;

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

    public Uni<PratoDTO> findById(Long prato_id) {
        return pratoRepository
                .findById(prato_id)
                .map(pratoMapper::toDTO);
    }
}
