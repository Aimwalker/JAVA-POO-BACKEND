package com.br.pdvpostocombustivel.api.cliente.dto;

import com.br.pdvpostocombustivel.enums.TipoPessoa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        String nomeCompleto,
        String cpfCnpj,
        String email,
        String telefone, // Novo campo
        LocalDate dataNascimento,
        TipoPessoa tipoPessoa, // Novo campo
        BigDecimal limiteCredito,
        BigDecimal saldoAtual
) {
}
