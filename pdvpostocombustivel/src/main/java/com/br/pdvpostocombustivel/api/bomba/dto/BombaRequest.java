package com.br.pdvpostocombustivel.api.bomba.dto;

import com.br.pdvpostocombustivel.enums.StatusBomba;

public record BombaRequest(
        Integer numero,
        Long tanqueId,
        StatusBomba status
) {
}
