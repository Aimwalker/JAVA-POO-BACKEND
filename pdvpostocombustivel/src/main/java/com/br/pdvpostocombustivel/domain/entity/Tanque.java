package com.br.pdvpostocombustivel.domain.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_tanque")
public class Tanque implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double capacidade;

    @Column(nullable = false)
    private Double nivelAtual;

    @ManyToOne
    @JoinColumn(name = "combustivel_id", nullable = false)
    private Produto combustivel; // Associação com a entidade Produto

    private LocalDateTime ultimaLeitura;

    private LocalDateTime dataUltimaEntrega;

    public Tanque() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Double capacidade) {
        this.capacidade = capacidade;
    }

    public Double getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(Double nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public Produto getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Produto combustivel) {
        this.combustivel = combustivel;
    }

    public LocalDateTime getUltimaLeitura() {
        return ultimaLeitura;
    }

    public void setUltimaLeitura(LocalDateTime ultimaLeitura) {
        this.ultimaLeitura = ultimaLeitura;
    }

    public LocalDateTime getDataUltimaEntrega() {
        return dataUltimaEntrega;
    }

    public void setDataUltimaEntrega(LocalDateTime dataUltimaEntrega) {
        this.dataUltimaEntrega = dataUltimaEntrega;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tanque tanque = (Tanque) o;
        return Objects.equals(id, tanque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
