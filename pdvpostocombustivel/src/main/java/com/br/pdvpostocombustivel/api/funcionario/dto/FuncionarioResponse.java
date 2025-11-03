package com.br.pdvpostocombustivel.api.funcionario.dto;

import java.time.LocalDate;
import java.util.Set;

public record FuncionarioResponse(
        Long id,
        String nomeCompleto,
        String cpfCnpj,
        String email,
        LocalDate dataNascimento,
        Set<String> acessos // Ex: ["ROLE_GERENTE", "ROLE_FUNCIONARIO"]
) {
}
