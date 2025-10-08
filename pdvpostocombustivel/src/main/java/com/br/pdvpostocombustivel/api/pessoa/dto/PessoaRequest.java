package com.br.pdvpostocombustivel.api.pessoa.dto;

import com.br.pdvpostocombustivel.enums.TipoPessoa;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

// Para entrada
public record PessoaRequest(
        String nomeCompleto,
        String cpfCnpj,
        Long numeroCtps,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataNascimento,
        TipoPessoa tipoPessoa)
{}