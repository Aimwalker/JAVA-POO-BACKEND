package com.br.pdvpostocombustivel.domain.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_abastecimento")
public class Abastecimento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bomba_id", nullable = false)
    private Bomba bomba;

    @Column(nullable = false)
    private Double litrosAbastecidos;

    @Column(nullable = false)
    private BigDecimal valorLitro; // Preço do combustível no momento do abastecimento

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "venda_id") // Pode ser nulo se o abastecimento não for pago imediatamente
    private Venda venda;

    public Abastecimento() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bomba getBomba() {
        return bomba;
    }

    public void setBomba(Bomba bomba) {
        this.bomba = bomba;
    }

    public Double getLitrosAbastecidos() {
        return litrosAbastecidos;
    }

    public void setLitrosAbastecidos(Double litrosAbastecidos) {
        this.litrosAbastecidos = litrosAbastecidos;
    }

    public BigDecimal getValorLitro() {
        return valorLitro;
    }

    public void setValorLitro(BigDecimal valorLitro) {
        this.valorLitro = valorLitro;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abastecimento that = (Abastecimento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
