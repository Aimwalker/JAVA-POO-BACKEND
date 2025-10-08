package com.br.pdvpostocombustivel.api.pessoa;


import com.br.pdvpostocombustivel.api.pessoa.dto.PessoaRequest;
import com.br.pdvpostocombustivel.api.pessoa.dto.PessoaResponse;
import com.br.pdvpostocombustivel.domain.entity.Pessoa;
import com.br.pdvpostocombustivel.domain.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PessoaService {

    // implementa a interface repository de pessoa
    private final PessoaRepository repository;


    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public PessoaResponse create(PessoaRequest req) {
        Pessoa novaPessoa = toEntity(req);
        return toResponse(repository.save(novaPessoa));
    }

    // READ by ID - validar a utilização desse método
    @Transactional(readOnly = true)
    public PessoaResponse getById(Long id) {
        Pessoa p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada. id=" + id));
        return toResponse(p);
    }

    // READ by CPF/CNPJ
    @Transactional(readOnly = true)
    public PessoaResponse getByCpfCnpj(String cpfCnpj) {
        Pessoa p = repository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada. cpfCnpj=" + cpfCnpj));
        return toResponse(p);
    }

    // LIST paginado
    @Transactional(readOnly = true)
    public Page<PessoaResponse> list(int page, int size, String sortBy, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return repository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE  - substitui todos os campos
    public PessoaResponse update(Long id, PessoaRequest req) {
        Pessoa p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada. id=" + id));

        if (req.cpfCnpj() != null && !req.cpfCnpj().equals(p.getCpfCnpj())) {
            validarUnicidadeCpfCnpj(req.cpfCnpj(), id);
        }

        p.setNomeCompleto(req.nomeCompleto());
        p.setCpfCnpj(req.cpfCnpj());
        p.setNumeroCtps(req.numeroCtps());
        p.setDataNascimento(req.dataNascimento());

        return toResponse(repository.save(p));
    }

    // PATCH - atualiza apenas campos não nulos
    public PessoaResponse patch(Long id, PessoaRequest req) {
        Pessoa p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada. id=" + id));

        if (req.nomeCompleto() != null)  p.setNomeCompleto(req.nomeCompleto());
        if (req.cpfCnpj() != null) {
            if (!req.cpfCnpj().equals(p.getCpfCnpj())) {
                validarUnicidadeCpfCnpj(req.cpfCnpj(), id);
            }
            p.setCpfCnpj(req.cpfCnpj());
        }
        if (req.numeroCtps() != null)    p.setNumeroCtps(req.numeroCtps());
        if (req.dataNascimento() != null) p.setDataNascimento(req.dataNascimento());

        return toResponse(repository.save(p));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Pessoa não encontrada. id=" + id);
        }
        repository.deleteById(id);
    }

    // ---------- Helpers ----------
    private void validarUnicidadeCpfCnpj(String cpfCnpj, Long idAtual) {
        repository.findByCpfCnpj(cpfCnpj).ifPresent(existente -> {
            if (idAtual == null || !existente.getId().equals(idAtual)) {
                throw new DataIntegrityViolationException("CPF/CNPJ já cadastrado: " + cpfCnpj);
            }
        });
    }

    private Pessoa toEntity(PessoaRequest req) {
        return new Pessoa(
                req.nomeCompleto(),
                req.cpfCnpj(),
                req.numeroCtps(),
                req.dataNascimento(),
                req.tipoPessoa()
        );
    }

    private PessoaResponse toResponse(Pessoa p) {
        return new PessoaResponse(
                p.getNomeCompleto(),
                p.getCpfCnpj(),
                p.getNumeroCtps(),
                p.getDataNascimento()
        );
    }
}