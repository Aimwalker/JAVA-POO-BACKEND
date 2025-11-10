package com.br.pdvpostocombustivel.api.venda.dto;

import com.br.pdvpostocombustivel.enums.FormaPagamento;
import com.br.pdvpostocombustivel.enums.StatusVenda;
import com.fasterxml.jackson.annotation.JsonFormat; // Importar esta anotação

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaFuncionarioResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Força o formato de serialização
        LocalDateTime data,
        BigDecimal valorTotal,
        Long funcionarioId,
        String funcionarioNome,
        Long clienteId,
        String nomeCliente,
        Long caixaId,
        FormaPagamento formaPagamento,
        StatusVenda status,
        List<ItemVendaFuncionarioResponse> itens
) {
}
