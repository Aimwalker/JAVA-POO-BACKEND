package com.br.pdvpostocombustivel.api.venda.dto;

import com.br.pdvpostocombustivel.enums.FormaPagamento;

import java.util.List;

public record VendaRequest(
        Long funcionarioId,
        Long clienteId, // Opcional, usado para CONTA_CLIENTE
        FormaPagamento formaPagamento,
        List<ItemVendaRequest> itens
) {
}
