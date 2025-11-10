package com.br.pdvpostocombustivel.api.bomba;

import com.br.pdvpostocombustivel.api.bomba.dto.BombaRequest;
import com.br.pdvpostocombustivel.api.bomba.dto.BombaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/bombas")
public class BombaController {

    private final BombaService bombaService;

    public BombaController(BombaService bombaService) {
        this.bombaService = bombaService;
    }

    @PostMapping
    public ResponseEntity<BombaResponse> create(@RequestBody BombaRequest request) {
        BombaResponse response = bombaService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<BombaResponse>> list(@PageableDefault(size = 10, sort = {"numeroBombaFisica", "numeroBico"}) Pageable pageable) { // CORRIGIDO AQUI
        Page<BombaResponse> page = bombaService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BombaResponse> getById(@PathVariable Long id) {
        BombaResponse response = bombaService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BombaResponse> update(@PathVariable Long id, @RequestBody BombaRequest request) {
        BombaResponse response = bombaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bombaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
