package com.br.pdvpostocombustivel.api.cliente.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        String nomeCompleto,
        String cpfCnpj,
        String email,
        LocalDate dataNascimento,
        BigDecimal limiteCredito,
        BigDecimal saldoAtual
) {
}
