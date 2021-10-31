package com.github.brutaleo.ifood.marketplace.service;

import com.github.brutaleo.ifood.marketplace.dto.CarrinhoDTO;
import com.github.brutaleo.ifood.marketplace.dto.CarrinhoMapper;
import com.github.brutaleo.ifood.marketplace.repository.CarrinhoRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoService (CarrinhoRepository carrinhoRepository, CarrinhoMapper carrinhoMapper) {
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoMapper = carrinhoMapper;
    }

    public Uni<List<CarrinhoDTO>> findByCliente(String cliente) {
        return carrinhoRepository
                .list("#Carrinho.getByCliente", cliente)
                .map(carrinhoMapper::toDTOList);
    }
}
