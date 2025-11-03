package com.br.pdvpostocombustivel.api.cliente.dto;

import java.math.BigDecimal;

public record PagamentoRequest(
        BigDecimal valor
) {
}
