package com.github.brutaleo.ifood.marketplace.dto;

import com.github.brutaleo.ifood.marketplace.model.Prato;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface PratoMapper {
    PratoDTO toDTO(Prato prato);
    Prato toPrato(PratoDTO dto);

    default List<PratoDTO> toDTOList(List<Prato> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
    default List<Prato> toPratoList(List<PratoDTO> list) {
        return list.stream().map(this::toPrato).collect(Collectors.toList());
    }
}

