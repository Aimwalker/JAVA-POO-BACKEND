package com.br.pdvpostocombustivel.api.caixa.dto;

import com.br.pdvpostocombustivel.enums.StatusCaixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaixaResponse(
        Long id,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        BigDecimal valorInicial,
        BigDecimal valorFinal,
        Long usuarioAberturaId,
        String usuarioAberturaNome,
        Long usuarioFechamentoId,
        String usuarioFechamentoNome,
        StatusCaixa status
) {
}
