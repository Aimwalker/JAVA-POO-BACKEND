package com.br.pdvpostocombustivel.api.funcionario.dto;

public record RegistroRequest(
    String nomeCompleto,
    String email,
    String senha,
    String cpfCnpj,
    String dataNascimento,
    String tipoPessoa
) {
}
