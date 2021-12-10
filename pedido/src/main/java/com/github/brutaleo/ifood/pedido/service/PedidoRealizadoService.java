package com.github.brutaleo.ifood.pedido.service;

import com.github.brutaleo.ifood.pedido.dto.PedidoRealizadoDTO;
import com.github.brutaleo.ifood.pedido.dto.PratoPedidoDTO;
import com.github.brutaleo.ifood.pedido.model.Pedido;
import com.github.brutaleo.ifood.pedido.model.Prato;
import com.github.brutaleo.ifood.pedido.model.Restaurante;
import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class PedidoRealizadoService {

    @Incoming("pedidos")
    public void recebePedidos(PedidoRealizadoDTO dto) {
        System.out.println("------------------");
        System.out.println(dto);

        Pedido pedido = new Pedido();
        pedido.cliente = dto.cliente;

        pedido.pratos = new ArrayList<>();
        dto.pratos.forEach(prato -> pedido.pratos.add(from(prato)));
        Restaurante restaurante = new Restaurante();
        restaurante.nome = dto.restaurante.nome;
        pedido.restaurante = restaurante;
        pedido.persist();

    }

    private Prato from(PratoPedidoDTO prato) {
        Prato p = new Prato();
        p.nome = prato.nome;
        p.descricao = prato.descricao;
        p.preco = new Decimal128(prato.preco);

        return p;
    }
}
