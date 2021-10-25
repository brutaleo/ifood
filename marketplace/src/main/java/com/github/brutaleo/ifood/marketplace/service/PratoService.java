package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PratoMapper;
import com.github.brutaleo.ifood.marketplace.model.Prato;
import com.github.brutaleo.ifood.marketplace.repository.PratoRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PratoService {

    private final PratoRepository pratoRepository;
    private final PratoMapper pratoMapper;

    public PratoService(PratoRepository pratoRepository, PratoMapper pratoMapper) {
        this.pratoRepository = pratoRepository;
        this.pratoMapper = pratoMapper;
    }
    public Uni<List<PratoDTO>> findAll() {
        return pratoRepository.listAll().map(pratoMapper::toDTOList);
    }
}
