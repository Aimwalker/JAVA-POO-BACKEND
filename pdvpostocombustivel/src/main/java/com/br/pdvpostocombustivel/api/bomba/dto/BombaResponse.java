package com.br.pdvpostocombustivel.api.bomba.dto;

import com.br.pdvpostocombustivel.enums.StatusBomba;

public record BombaResponse(
        Long id,
        Integer numeroBombaFisica, // Novo campo
        Integer numeroBico,        // Renomeado de 'numero'
        Long tanqueId,
        Long combustivelId,
        String nomeCombustivel,
        StatusBomba status
) {
}
