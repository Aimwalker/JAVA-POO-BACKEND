package com.br.pdvpostocombustivel.domain.entity;

import com.br.pdvpostocombustivel.enums.StatusCaixa;
import com.fasterxml.jackson.annotation.JsonFormat; // Importar esta anotação
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_caixa")
public class Caixa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Força o formato de serialização
    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Força o formato de serialização
    private LocalDateTime dataFechamento;

    @Column(nullable = false)
    private BigDecimal valorInicial;

    private BigDecimal valorFinal;

    @ManyToOne
    @JoinColumn(name = "usuario_abertura_id", nullable = false)
    private Funcionario usuarioAbertura;

    @ManyToOne
    @JoinColumn(name = "usuario_fechamento_id")
    private Funcionario usuarioFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCaixa status;

    public Caixa() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Funcionario getUsuarioAbertura() {
        return usuarioAbertura;
    }

    public void setUsuarioAbertura(Funcionario usuarioAbertura) {
        this.usuarioAbertura = usuarioAbertura;
    }

    public Funcionario getUsuarioFechamento() {
        return usuarioFechamento;
    }

    public void setUsuarioFechamento(Funcionario usuarioFechamento) {
        this.usuarioFechamento = usuarioFechamento;
    }

    public StatusCaixa getStatus() {
        return status;
    }

    public void setStatus(StatusCaixa status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caixa caixa = (Caixa) o;
        return Objects.equals(id, caixa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
