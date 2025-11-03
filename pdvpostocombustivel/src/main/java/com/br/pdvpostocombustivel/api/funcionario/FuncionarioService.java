package com.br.pdvpostocombustivel.api.funcionario;

import com.br.pdvpostocombustivel.api.funcionario.dto.FuncionarioRequest;
import com.br.pdvpostocombustivel.api.funcionario.dto.FuncionarioResponse;
import com.br.pdvpostocombustivel.domain.entity.Acesso;
import com.br.pdvpostocombustivel.domain.entity.Funcionario;
import com.br.pdvpostocombustivel.domain.repository.AcessoRepository;
import com.br.pdvpostocombustivel.domain.repository.FuncionarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AcessoRepository acessoRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, AcessoRepository acessoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.acessoRepository = acessoRepository;
    }

    // CREATE
    @Transactional
    public FuncionarioResponse create(FuncionarioRequest req) {
        if (funcionarioRepository.existsByCpfCnpj(req.cpfCnpj())) {
            throw new DataIntegrityViolationException("CPF/CNPJ já cadastrado: " + req.cpfCnpj());
        }
        Funcionario novoFuncionario = toEntity(req);
        Funcionario funcionarioSalvo = funcionarioRepository.save(novoFuncionario);
        return toResponse(funcionarioSalvo);
    }

    // READ by ID
    public FuncionarioResponse getById(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. id=" + id));
        return toResponse(f);
    }

    // LIST paginado
    public Page<FuncionarioResponse> list(Pageable pageable) {
        return funcionarioRepository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE
    @Transactional
    public FuncionarioResponse update(Long id, FuncionarioRequest req) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. id=" + id));

        if (!f.getCpfCnpj().equals(req.cpfCnpj()) && funcionarioRepository.existsByCpfCnpj(req.cpfCnpj())) {
            throw new DataIntegrityViolationException("CPF/CNPJ já cadastrado: " + req.cpfCnpj());
        }

        Set<Acesso> acessos = req.acessos().stream()
                .map(nomeAcesso -> acessoRepository.findByDescricao(nomeAcesso)
                        .orElseThrow(() -> new IllegalArgumentException("Acesso não encontrado: " + nomeAcesso)))
                .collect(Collectors.toSet());

        f.setNomeCompleto(req.nomeCompleto());
        f.setCpfCnpj(req.cpfCnpj());
        f.setEmail(req.email());
        f.setDataNascimento(req.dataNascimento());
        f.setTipoPessoa(req.tipoPessoa());
        f.setNumeroCtps(req.numeroCtps());

        if (req.senha() != null && !req.senha().isBlank()) {
             f.setSenha(req.senha());
        }
        f.setAcessos(acessos);

        Funcionario funcionarioAtualizado = funcionarioRepository.save(f);
        return toResponse(funcionarioAtualizado);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Funcionário não encontrado. id=" + id);
        }
        funcionarioRepository.deleteById(id);
    }

    // ---------- Helpers ----------

    private Funcionario toEntity(FuncionarioRequest req) {
        Set<Acesso> acessos = req.acessos().stream()
                .map(nomeAcesso -> acessoRepository.findByDescricao(nomeAcesso)
                        .orElseThrow(() -> new IllegalArgumentException("Acesso não encontrado: " + nomeAcesso)))
                .collect(Collectors.toSet());

        Funcionario f = new Funcionario();
        f.setNomeCompleto(req.nomeCompleto());
        f.setCpfCnpj(req.cpfCnpj());
        f.setEmail(req.email());
        f.setDataNascimento(req.dataNascimento());
        f.setTipoPessoa(req.tipoPessoa());
        f.setNumeroCtps(req.numeroCtps());
        f.setSenha(req.senha());
        f.setAcessos(acessos);

        return f;
    }

    private FuncionarioResponse toResponse(Funcionario f) {
        Set<String> nomesAcessos = f.getAcessos().stream()
                .map(Acesso::getDescricao)
                .collect(Collectors.toSet());

        return new FuncionarioResponse(
                f.getId(),
                f.getNomeCompleto(),
                f.getCpfCnpj(),
                f.getEmail(),
                f.getDataNascimento(),
                nomesAcessos
        );
    }
}
