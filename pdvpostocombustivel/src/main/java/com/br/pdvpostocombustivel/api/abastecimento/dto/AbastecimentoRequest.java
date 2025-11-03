package com.br.pdvpostocombustivel.api.abastecimento.dto;

public record AbastecimentoRequest(
        Long bombaId,
        Double litrosAbastecidos
) {
}
