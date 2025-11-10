package com.br.pdvpostocombustivel.api.login;

import com.br.pdvpostocombustivel.api.login.dto.LoginRequest;
import com.br.pdvpostocombustivel.api.login.dto.LoginResponse;
import com.br.pdvpostocombustivel.domain.entity.Acesso;
import com.br.pdvpostocombustivel.domain.entity.Funcionario;
import com.br.pdvpostocombustivel.domain.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LoginService {

    private final FuncionarioRepository funcionarioRepository;

    public LoginService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Optional<LoginResponse> autenticar(LoginRequest req) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(req.email());

        if (funcionarioOpt.isPresent()) {
            Funcionario funcionario = funcionarioOpt.get();
            // Verificação de senha em texto puro (APENAS PARA TESTE)
            if (funcionario.getSenha().equals(req.senha())) {
                return Optional.of(toResponse(funcionario));
            }
        }
        return Optional.empty();
    }

    private LoginResponse toResponse(Funcionario f) {
        return new LoginResponse(
                f.getId(),
                f.getNomeCompleto(),
                f.getEmail(),
                f.getAcessos().stream().map(Acesso::getDescricao).collect(Collectors.toSet())
        );
    }
}
