package com.br.pdvpostocombustivel.api.caixa.dto;

import java.math.BigDecimal;

public record CaixaAberturaRequest(
        Long usuarioAberturaId,
        BigDecimal valorInicial
) {
}
