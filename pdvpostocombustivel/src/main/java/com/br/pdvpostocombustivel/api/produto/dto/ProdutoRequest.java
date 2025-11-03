package com.br.pdvpostocombustivel.api.produto.dto;

import com.br.pdvpostocombustivel.enums.TipoProduto;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String descricao,
        String codigoBarras,
        BigDecimal precoVenda,
        BigDecimal custo,
        TipoProduto tipo,
        Double estoqueAtual
) {
}
