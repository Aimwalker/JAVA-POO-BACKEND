package com.br.pdvpostocombustivel.domain.entity;

import com.br.pdvpostocombustivel.enums.StatusBomba;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_bomba", uniqueConstraints = {
        @UniqueConstraint(name = "uk_bomba_fisica_bico", columnNames = {"numero_bomba_fisica", "numero_bico"})
})
public class Bomba implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_bomba_fisica", nullable = false)
    private Integer numeroBombaFisica; // Identifica a bomba física (ex: Bomba 1, Bomba 2)

    @Column(name = "numero_bico", nullable = false)
    private Integer numeroBico; // Identifica o bico dentro da bomba física (ex: Bico 1, Bico 2)

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

    public Integer getNumeroBombaFisica() {
        return numeroBombaFisica;
    }

    public void setNumeroBombaFisica(Integer numeroBombaFisica) {
        this.numeroBombaFisica = numeroBombaFisica;
    }

    public Integer getNumeroBico() {
        return numeroBico;
    }

    public void setNumeroBico(Integer numeroBico) {
        this.numeroBico = numeroBico;
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
