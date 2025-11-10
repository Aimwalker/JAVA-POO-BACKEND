package com.br.pdvpostocombustivel.api.cliente.dto;

import com.br.pdvpostocombustivel.enums.TipoMovimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentoContaClienteResponse(
        Long id,
        TipoMovimento tipo,
        BigDecimal valor,
        LocalDateTime dataHora,
        String descricao
) {
}
