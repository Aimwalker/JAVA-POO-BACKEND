package com.br.pdvpostocombustivel.api.login.dto;

import java.util.Set;

public record LoginResponse(
        Long id,
        String nome,
        String email,
        Set<String> acessos
) {
}
