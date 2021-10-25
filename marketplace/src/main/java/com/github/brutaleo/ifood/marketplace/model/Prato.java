package com.github.brutaleo.ifood.marketplace.model;

import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.vertx.mutiny.pgclient.PgPool;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "prato")
public class Prato extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public String nome;

    public String descricao;

    public BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    public Restaurante restaurante;


    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
