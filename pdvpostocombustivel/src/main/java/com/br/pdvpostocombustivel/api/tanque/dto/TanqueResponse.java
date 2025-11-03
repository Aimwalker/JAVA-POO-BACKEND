package com.br.pdvpostocombustivel.api.tanque.dto;

import java.time.LocalDateTime;

public record TanqueResponse(
        Long id,
        Double capacidade,
        Double nivelAtual,
        Long combustivelId,
        String nomeCombustivel,
        LocalDateTime ultimaLeitura,
        LocalDateTime dataUltimaEntrega
) {
}
