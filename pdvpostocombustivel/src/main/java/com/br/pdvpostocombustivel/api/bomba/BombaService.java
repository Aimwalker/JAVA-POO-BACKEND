package com.br.pdvpostocombustivel.api.bomba;

import com.br.pdvpostocombustivel.api.bomba.dto.BombaRequest;
import com.br.pdvpostocombustivel.api.bomba.dto.BombaResponse;
import com.br.pdvpostocombustivel.domain.entity.Bomba;
import com.br.pdvpostocombustivel.domain.entity.Tanque;
import com.br.pdvpostocombustivel.domain.repository.BombaRepository;
import com.br.pdvpostocombustivel.domain.repository.TanqueRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BombaService {

    private final BombaRepository bombaRepository;
    private final TanqueRepository tanqueRepository;

    public BombaService(BombaRepository bombaRepository, TanqueRepository tanqueRepository) {
        this.bombaRepository = bombaRepository;
        this.tanqueRepository = tanqueRepository;
    }

    // CREATE
    @Transactional
    public BombaResponse create(BombaRequest req) {
        if (bombaRepository.existsByNumero(req.numero())) {
            throw new DataIntegrityViolationException("Número de bomba já cadastrado: " + req.numero());
        }
        Bomba novaBomba = toEntity(new Bomba(), req);
        Bomba bombaSalva = bombaRepository.save(novaBomba);
        return toResponse(bombaSalva);
    }

    // READ by ID
    public BombaResponse getById(Long id) {
        Bomba b = bombaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bomba não encontrada. id=" + id));
        return toResponse(b);
    }

    // LIST paginado
    public Page<BombaResponse> list(Pageable pageable) {
        return bombaRepository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE
    @Transactional
    public BombaResponse update(Long id, BombaRequest req) {
        Bomba b = bombaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bomba não encontrada. id=" + id));

        if (!b.getNumero().equals(req.numero()) && bombaRepository.existsByNumero(req.numero())) {
            throw new DataIntegrityViolationException("Número de bomba já cadastrado: " + req.numero());
        }

        Bomba bombaAtualizada = toEntity(b, req);
        bombaRepository.save(bombaAtualizada);
        return toResponse(bombaAtualizada);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!bombaRepository.existsById(id)) {
            throw new IllegalArgumentException("Bomba não encontrada. id=" + id);
        }
        bombaRepository.deleteById(id);
    }

    // ---------- Helpers ----------

    private Bomba toEntity(Bomba b, BombaRequest req) {
        Tanque tanque = tanqueRepository.findById(req.tanqueId())
                .orElseThrow(() -> new IllegalArgumentException("Tanque não encontrado. id=" + req.tanqueId()));

        b.setNumero(req.numero());
        b.setTanque(tanque);
        b.setStatus(req.status());
        return b;
    }

    private BombaResponse toResponse(Bomba b) {
        return new BombaResponse(
                b.getId(),
                b.getNumero(),
                b.getTanque().getId(),
                b.getTanque().getCombustivel().getId(),
                b.getTanque().getCombustivel().getNome(),
                b.getStatus()
        );
    }
}
