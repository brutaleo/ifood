package com.github.brutaleo.ifood.marketplace.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "carrinho")
@NamedQuery(name = "Carrinho.getByCliente",
        query = "SELECT c FROM Carrinho c where c.cliente = ?1")
public class Carrinho extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public String cliente;

    @OneToMany(mappedBy = "carrinho")
    private Collection<Prato> prato;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Prato> getPrato() {
        return prato;
    }

    public void setPrato(Collection<Prato> prato) {
        this.prato = prato;
    }
}
