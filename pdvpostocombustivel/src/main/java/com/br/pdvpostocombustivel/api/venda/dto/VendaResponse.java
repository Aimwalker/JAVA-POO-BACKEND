package com.br.pdvpostocombustivel.api.venda.dto;

import com.br.pdvpostocombustivel.enums.FormaPagamento;
import com.br.pdvpostocombustivel.enums.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
        Long id,
        LocalDateTime data,
        BigDecimal valorTotal,
        Long funcionarioId,
        String funcionarioNome,
        Long clienteId, // Novo campo
        String nomeCliente, // Novo campo
        FormaPagamento formaPagamento,
        StatusVenda status,
        List<ItemVendaResponse> itens
) {
}
