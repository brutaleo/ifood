package com.github.brutaleo.ifood.marketplace.dto;

import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import io.vertx.mutiny.sqlclient.Row;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface CarrinhoMapper {

    CarrinhoDTO toDTO(Carrinho carrinho);

    Carrinho toCarrinho(CarrinhoDTO dto);

    default List<CarrinhoDTO> toDTOList(List<Carrinho> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    default List<Carrinho> toCarrinhoList(List<CarrinhoDTO> list) {
        return list.stream().map(this::toCarrinho).collect(Collectors.toList());
    }

    default Carrinho toPratoCarrinho(Row row) {
        Carrinho carrinho = new Carrinho();
        carrinho.cliente = row.getString("cliente");
        carrinho.prato = row.getLong("prato");
        return carrinho;
    }
}
