package com.github.brutaleo.ifood.cadastro.dto;

import com.github.brutaleo.ifood.cadastro.model.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nomeFantasia", source = "nome")
    @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    RestauranteDTO toRestauranteDTO(Restaurante r);

    @Mapping(target = "nome", source = "nomeFantasia")
    Restaurante toRestaurante(AdicionarRestauranteDTO dto);

    @Mapping(target = "nome", source = "nomeFantasia")
    void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

}
