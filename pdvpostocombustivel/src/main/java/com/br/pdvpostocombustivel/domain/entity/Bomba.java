package com.br.pdvpostocombustivel.domain.entity;

import com.br.pdvpostocombustivel.enums.StatusBomba;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_bomba")
public class Bomba implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "tanque_id", nullable = false)
    private Tanque tanque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBomba status;

    public Bomba() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Tanque getTanque() {
        return tanque;
    }

    public void setTanque(Tanque tanque) {
        this.tanque = tanque;
    }

    public StatusBomba getStatus() {
        return status;
    }

    public void setStatus(StatusBomba status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bomba bomba = (Bomba) o;
        return Objects.equals(id, bomba.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
