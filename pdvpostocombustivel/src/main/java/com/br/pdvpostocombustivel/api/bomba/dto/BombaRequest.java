package com.br.pdvpostocombustivel.api.bomba.dto;

import com.br.pdvpostocombustivel.enums.StatusBomba;

public record BombaRequest(
        Integer numeroBombaFisica, // Novo campo
        Integer numeroBico,        // Renomeado de 'numero'
        Long tanqueId,
        StatusBomba status
) {
}
