package com.br.pdvpostocombustivel.api.caixa.dto;

import java.math.BigDecimal;

public record CaixaFechamentoRequest(
        Long usuarioFechamentoId,
        BigDecimal valorFinal
) {
}
