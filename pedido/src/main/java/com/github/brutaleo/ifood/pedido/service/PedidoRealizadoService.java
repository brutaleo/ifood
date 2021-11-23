package com.github.brutaleo.ifood.pedido.service;

import com.github.brutaleo.ifood.pedido.dto.PedidoRealizadoDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRealizadoService {

    @Incoming("pedidos")
    public void recebePedidos(PedidoRealizadoDTO dto) {
        System.out.println(dto);

    }
}
