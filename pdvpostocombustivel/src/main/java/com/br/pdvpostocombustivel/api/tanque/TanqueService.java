package com.br.pdvpostocombustivel.api.tanque;

import com.br.pdvpostocombustivel.api.tanque.dto.TanqueRequest;
import com.br.pdvpostocombustivel.api.tanque.dto.TanqueResponse;
import com.br.pdvpostocombustivel.domain.entity.Produto;
import com.br.pdvpostocombustivel.domain.entity.Tanque;
import com.br.pdvpostocombustivel.domain.repository.ProdutoRepository;
import com.br.pdvpostocombustivel.domain.repository.TanqueRepository;
import com.br.pdvpostocombustivel.enums.TipoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TanqueService {

    private final TanqueRepository tanqueRepository;
    private final ProdutoRepository produtoRepository;

    public TanqueService(TanqueRepository tanqueRepository, ProdutoRepository produtoRepository) {
        this.tanqueRepository = tanqueRepository;
        this.produtoRepository = produtoRepository;
    }

    // CREATE
    @Transactional
    public TanqueResponse create(TanqueRequest req) {
        Tanque novoTanque = toEntity(new Tanque(), req);
        Tanque tanqueSalvo = tanqueRepository.save(novoTanque);
        return toResponse(tanqueSalvo);
    }

    // READ by ID
    public TanqueResponse getById(Long id) {
        Tanque t = tanqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tanque não encontrado. id=" + id));
        return toResponse(t);
    }

    // LIST paginado
    public Page<TanqueResponse> list(Pageable pageable) {
        return tanqueRepository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE
    @Transactional
    public TanqueResponse update(Long id, TanqueRequest req) {
        Tanque t = tanqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tanque não encontrado. id=" + id));

        Tanque tanqueAtualizado = toEntity(t, req);
        tanqueRepository.save(tanqueAtualizado);
        return toResponse(tanqueAtualizado);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!tanqueRepository.existsById(id)) {
            throw new IllegalArgumentException("Tanque não encontrado. id=" + id);
        }
        tanqueRepository.deleteById(id);
    }

    // ---------- Helpers ----------

    private Tanque toEntity(Tanque t, TanqueRequest req) {
        Produto combustivel = produtoRepository.findById(req.combustivelId())
                .orElseThrow(() -> new IllegalArgumentException("Combustível (Produto) não encontrado. id=" + req.combustivelId()));

        if (combustivel.getTipo() != TipoProduto.COMBUSTIVEL) {
            throw new IllegalArgumentException("O produto informado não é do tipo COMBUSTIVEL.");
        }

        t.setCapacidade(req.capacidade());
        t.setNivelAtual(req.nivelAtual());
        t.setCombustivel(combustivel);
        // Os campos de data não são definidos aqui, seriam atualizados por outras operações.

        return t;
    }

    private TanqueResponse toResponse(Tanque t) {
        return new TanqueResponse(
                t.getId(),
                t.getCapacidade(),
                t.getNivelAtual(),
                t.getCombustivel().getId(),
                t.getCombustivel().getNome(),
                t.getUltimaLeitura(),
                t.getDataUltimaEntrega()
        );
    }
}
