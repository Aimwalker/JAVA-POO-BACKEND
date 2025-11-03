package com.br.pdvpostocombustivel.api.cliente.dto;

import com.br.pdvpostocombustivel.enums.TipoPessoa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteRequest(
        // Dados de Pessoa
        String nomeCompleto,
        String cpfCnpj,
        String email,
        LocalDate dataNascimento,
        TipoPessoa tipoPessoa,

        // Dados de Cliente
        BigDecimal limiteCredito
) {
}
