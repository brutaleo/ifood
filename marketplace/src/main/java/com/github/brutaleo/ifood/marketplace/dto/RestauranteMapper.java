package com.github.brutaleo.ifood.marketplace.dto;

import com.github.brutaleo.ifood.marketplace.model.Restaurante;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    RestauranteDTO toDTO(Restaurante restaurante);

    Restaurante toRestaurante(RestauranteDTO dto);

    default List<RestauranteDTO> toDTOList(List<Restaurante> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    default List<Restaurante> toPratoList(List<RestauranteDTO> list) {
        return list.stream().map(this::toRestaurante).collect(Collectors.toList());
    }
}


