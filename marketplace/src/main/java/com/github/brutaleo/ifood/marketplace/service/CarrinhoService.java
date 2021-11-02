package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.CarrinhoDTO;
import com.github.brutaleo.ifood.marketplace.dto.CarrinhoMapper;
import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import com.github.brutaleo.ifood.marketplace.model.Prato;
import com.github.brutaleo.ifood.marketplace.repository.CarrinhoRepository;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CarrinhoService {
    @Inject
    CarrinhoRepository carrinhoRepository;
    @Inject
    CarrinhoMapper carrinhoMapper;
    @Inject
    PratoService pratoService;

    public Uni<List<CarrinhoDTO>> findByCliente(String cliente) {
        return carrinhoRepository
                .list("#Carrinho.getByCliente", cliente)
                .map(carrinhoMapper::toDTOList);
    }

    private Uni<CarrinhoDTO> findById(Long carrino_id) {
        return carrinhoRepository
                .findById(carrino_id)
                .map(carrinhoMapper::toDTO);
    }

    public Uni<Carrinho> adicionaPratoAoCarrinho(Long carrino_id, Long prato_id) {
        Uni<CarrinhoDTO> carrinho = findById(carrino_id);
        Uni<PratoDTO> prato = pratoService.findById(prato_id);


        return null;
    }

}
