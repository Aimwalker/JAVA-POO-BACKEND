package com.br.pdvpostocombustivel.api.caixa;

import com.br.pdvpostocombustivel.api.caixa.dto.CaixaAberturaRequest;
import com.br.pdvpostocombustivel.api.caixa.dto.CaixaFechamentoRequest;
import com.br.pdvpostocombustivel.api.caixa.dto.CaixaResponse;
import com.br.pdvpostocombustivel.domain.entity.Caixa;
import com.br.pdvpostocombustivel.domain.entity.Funcionario;
import com.br.pdvpostocombustivel.domain.repository.CaixaRepository;
import com.br.pdvpostocombustivel.domain.repository.FuncionarioRepository;
import com.br.pdvpostocombustivel.enums.StatusCaixa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CaixaService(CaixaRepository caixaRepository, FuncionarioRepository funcionarioRepository) {
        this.caixaRepository = caixaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    // ABRIR CAIXA
    @Transactional
    public CaixaResponse abrirCaixa(CaixaAberturaRequest req) {
        if (caixaRepository.findByStatus(StatusCaixa.ABERTO).isPresent()) {
            throw new IllegalStateException("Já existe um caixa aberto.");
        }

        Funcionario usuarioAbertura = funcionarioRepository.findById(req.usuarioAberturaId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário de abertura não encontrado. ID: " + req.usuarioAberturaId()));

        Caixa novoCaixa = new Caixa();
        novoCaixa.setDataAbertura(LocalDateTime.now());
        novoCaixa.setValorInicial(req.valorInicial());
        novoCaixa.setUsuarioAbertura(usuarioAbertura);
        novoCaixa.setStatus(StatusCaixa.ABERTO);

        Caixa caixaSalvo = caixaRepository.save(novoCaixa);
        return toResponse(caixaSalvo);
    }

    // FECHAR CAIXA
    @Transactional
    public CaixaResponse fecharCaixa(Long id, CaixaFechamentoRequest req) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caixa não encontrado. ID: " + id));

        if (caixa.getStatus() == StatusCaixa.FECHADO) {
            throw new IllegalStateException("O caixa já está fechado.");
        }

        Funcionario usuarioFechamento = funcionarioRepository.findById(req.usuarioFechamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário de fechamento não encontrado. ID: " + req.usuarioFechamentoId()));

        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setValorFinal(req.valorFinal());
        caixa.setUsuarioFechamento(usuarioFechamento);
        caixa.setStatus(StatusCaixa.FECHADO);

        Caixa caixaAtualizado = caixaRepository.save(caixa);
        return toResponse(caixaAtualizado);
    }

    // BUSCAR CAIXA ABERTO
    public CaixaResponse buscarCaixaAberto() {
        Caixa caixa = caixaRepository.findByStatus(StatusCaixa.ABERTO)
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto encontrado."));
        return toResponse(caixa);
    }

    // ---------- Helpers ----------

    private CaixaResponse toResponse(Caixa caixa) {
        return new CaixaResponse(
                caixa.getId(),
                caixa.getDataAbertura(),
                caixa.getDataFechamento(),
                caixa.getValorInicial(),
                caixa.getValorFinal(),
                caixa.getUsuarioAbertura().getId(),
                caixa.getUsuarioAbertura().getNomeCompleto(),
                caixa.getUsuarioFechamento() != null ? caixa.getUsuarioFechamento().getId() : null,
                caixa.getUsuarioFechamento() != null ? caixa.getUsuarioFechamento().getNomeCompleto() : null,
                caixa.getStatus()
        );
    }
}
