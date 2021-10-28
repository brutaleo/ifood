package com.github.brutaleo.ifood.pedido.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.List;

@MongoEntity(collection = "pedidos", database = "pedido")
public class Pedido extends PanacheMongoEntity {

    public String cliente;

    public Restaurante restaurante;

    public List<Prato> pratos;

    public String entregador;

    public Localizacao localizacaoEntregador;
}
