package com.br.pdvpostocombustivel.api.tanque.dto;

public record TanqueRequest(
        Double capacidade,
        Double nivelAtual,
        Long combustivelId
) {
}
