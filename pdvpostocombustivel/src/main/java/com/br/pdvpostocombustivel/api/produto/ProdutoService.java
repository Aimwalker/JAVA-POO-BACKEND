package com.br.pdvpostocombustivel.api.produto;

import com.br.pdvpostocombustivel.api.produto.dto.ProdutoRequest;
import com.br.pdvpostocombustivel.api.produto.dto.ProdutoResponse;
import com.br.pdvpostocombustivel.domain.entity.Produto;
import com.br.pdvpostocombustivel.domain.repository.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // CREATE
    @Transactional
    public ProdutoResponse create(ProdutoRequest req) {
        if (produtoRepository.existsByCodigoBarras(req.codigoBarras())) {
            throw new DataIntegrityViolationException("Código de barras já cadastrado: " + req.codigoBarras());
        }
        Produto novoProduto = toEntity(new Produto(), req);
        Produto produtoSalvo = produtoRepository.save(novoProduto);
        return toResponse(produtoSalvo);
    }

    // READ by ID
    public ProdutoResponse getById(Long id) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado. id=" + id));
        return toResponse(p);
    }

    // LIST paginado
    public Page<ProdutoResponse> list(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE
    @Transactional
    public ProdutoResponse update(Long id, ProdutoRequest req) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado. id=" + id));

        // Valida a unicidade do código de barras se ele for alterado
        if (!p.getCodigoBarras().equals(req.codigoBarras()) && produtoRepository.existsByCodigoBarras(req.codigoBarras())) {
            throw new DataIntegrityViolationException("Código de barras já cadastrado: " + req.codigoBarras());
        }

        Produto produtoAtualizado = toEntity(p, req);
        produtoRepository.save(produtoAtualizado);
        return toResponse(produtoAtualizado);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado. id=" + id);
        }
        produtoRepository.deleteById(id);
    }

    // ---------- Helpers ----------

    private Produto toEntity(Produto p, ProdutoRequest req) {
        p.setNome(req.nome());
        p.setDescricao(req.descricao());
        p.setCodigoBarras(req.codigoBarras());
        p.setPrecoVenda(req.precoVenda());
        p.setCusto(req.custo());
        p.setTipo(req.tipo());
        p.setEstoqueAtual(req.estoqueAtual());
        return p;
    }

    private ProdutoResponse toResponse(Produto p) {
        return new ProdutoResponse(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getCodigoBarras(),
                p.getPrecoVenda(),
                p.getCusto(),
                p.getTipo(),
                p.getEstoqueAtual()
        );
    }
}
