package com.br.pdvpostocombustivel.domain.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_cliente")
@PrimaryKeyJoinColumn(name = "id") // Chave primária será a mesma da tb_pessoa
public class Cliente extends Pessoa {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private BigDecimal limiteCredito;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContaCliente contaCliente;

    public Cliente() {
    }

    // Getters e Setters

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public ContaCliente getContaCliente() {
        return contaCliente;
    }

    public void setContaCliente(ContaCliente contaCliente) {
        this.contaCliente = contaCliente;
    }
}
