package com.br.pdvpostocombustivel.api.funcionario.dto;

import com.br.pdvpostocombustivel.enums.TipoPessoa;

import java.time.LocalDate;
import java.util.Set;

public record FuncionarioRequest(
        // Dados de Pessoa
        String nomeCompleto,
        String cpfCnpj,
        String email,
        LocalDate dataNascimento,
        TipoPessoa tipoPessoa,
        Long numeroCtps,

        // Dados de Funcionario
        String senha,
        Set<String> acessos // Ex: ["ROLE_GERENTE", "ROLE_FUNCIONARIO"]
) {
}
