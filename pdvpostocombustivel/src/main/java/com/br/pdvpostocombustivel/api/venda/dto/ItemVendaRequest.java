package com.br.pdvpostocombustivel.api.venda.dto;

public record ItemVendaRequest(
        Long produtoId,
        Double quantidade
) {
}
