package com.br.pdvpostocombustivel.api.abastecimento;

import com.br.pdvpostocombustivel.api.abastecimento.dto.AbastecimentoRequest;
import com.br.pdvpostocombustivel.api.abastecimento.dto.AbastecimentoResponse;
import com.br.pdvpostocombustivel.domain.entity.Abastecimento;
import com.br.pdvpostocombustivel.domain.entity.Bomba;
import com.br.pdvpostocombustivel.domain.entity.Produto;
import com.br.pdvpostocombustivel.domain.repository.AbastecimentoRepository;
import com.br.pdvpostocombustivel.domain.repository.BombaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final BombaRepository bombaRepository;

    public AbastecimentoService(AbastecimentoRepository abastecimentoRepository, BombaRepository bombaRepository) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.bombaRepository = bombaRepository;
    }

    // CREATE
    public AbastecimentoResponse create(AbastecimentoRequest req) {
        Bomba bomba = bombaRepository.findById(req.bombaId())
                .orElseThrow(() -> new IllegalArgumentException("Bomba não encontrada com o ID: " + req.bombaId()));

        Abastecimento novoAbastecimento = toEntity(req, bomba);
        Abastecimento abastecimentoSalvo = abastecimentoRepository.save(novoAbastecimento);
        return toResponse(abastecimentoSalvo);
    }

    // ---------- Helpers ----------

    private Abastecimento toEntity(AbastecimentoRequest req, Bomba bomba) {
        Produto combustivel = bomba.getTanque().getCombustivel();
        BigDecimal valorLitro = combustivel.getPrecoVenda();
        BigDecimal valorTotal = valorLitro.multiply(BigDecimal.valueOf(req.litrosAbastecidos()));

        Abastecimento a = new Abastecimento();
        a.setBomba(bomba);
        a.setLitrosAbastecidos(req.litrosAbastecidos());
        a.setValorLitro(valorLitro);
        a.setValorTotal(valorTotal);
        a.setDataHora(LocalDateTime.now());
        // O campo 'venda' fica nulo por enquanto. Ele seria preenchido em outra operação.

        return a;
    }

    private AbastecimentoResponse toResponse(Abastecimento a) {
        Produto combustivel = a.getBomba().getTanque().getCombustivel();

        return new AbastecimentoResponse(
                a.getId(),
                a.getBomba().getId(),
                a.getBomba().getNumero(),
                combustivel.getId(),
                combustivel.getNome(),
                a.getLitrosAbastecidos(),
                a.getValorLitro(),
                a.getValorTotal(),
                a.getDataHora()
        );
    }
}
