package com.github.brutaleo.ifood.pedido.util;

import com.github.brutaleo.ifood.pedido.dto.PedidoRealizadoDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoDeserializer extends ObjectMapperDeserializer<PedidoRealizadoDTO> {
    public PedidoDeserializer() {
        super(PedidoRealizadoDTO.class);
    }
}
