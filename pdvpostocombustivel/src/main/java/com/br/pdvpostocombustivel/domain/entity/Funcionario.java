package com.br.pdvpostocombustivel.domain.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_funcionario")
@PrimaryKeyJoinColumn(name = "id") // Chave primária da tabela tb_funcionario será a mesma da tb_pessoa
public class Funcionario extends Pessoa {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_funcionario_acesso",
            joinColumns = @JoinColumn(name = "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "acesso_id"))
    private Set<Acesso> acessos = new HashSet<>();

    public Funcionario() {
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Acesso> getAcessos() {
        return acessos;
    }

    public void setAcessos(Set<Acesso> acessos) {
        this.acessos = acessos;
    }
}
