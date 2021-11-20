package com.github.brutaleo.ifood.marketplace.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "restaurante")
public class Restaurante extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public String nome;

    @OneToOne
    @JoinColumn(name = "localizacao_id")
    public Localizacao localizacao;

    @OneToMany(mappedBy = "restaurante")
    private Collection<Prato> prato;

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void persist(PgPool pgPool) {

        pgPool
                .preparedQuery(
                        "insert into localizacao (id, latitude, longitude) values ($1, $2, $3)")
                .execute(
                        Tuple.of(
                                localizacao.getId(),
                                localizacao.latitude,
                                localizacao.longitude
                        )
                )
                .await()
                .indefinitely();

        pgPool
                .preparedQuery(
                        "insert into restaurante (id, nome, localizacao_id) values ($1, $2, $3)")
                .execute(
                        Tuple.of(
                                id,
                                nome,
                                localizacao.getId()
                        ))
                .await()
                .indefinitely();
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao=" + localizacao +
                '}';
    }
}
