package com.br.pdvpostocombustivel.api.tanque;

import com.br.pdvpostocombustivel.api.tanque.dto.TanqueRequest;
import com.br.pdvpostocombustivel.api.tanque.dto.TanqueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tanques")
public class TanqueController {

    private final TanqueService tanqueService;

    public TanqueController(TanqueService tanqueService) {
        this.tanqueService = tanqueService;
    }

    @PostMapping
    public ResponseEntity<TanqueResponse> create(@RequestBody TanqueRequest request) {
        TanqueResponse response = tanqueService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TanqueResponse>> list(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<TanqueResponse> page = tanqueService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TanqueResponse> getById(@PathVariable Long id) {
        TanqueResponse response = tanqueService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TanqueResponse> update(@PathVariable Long id, @RequestBody TanqueRequest request) {
        TanqueResponse response = tanqueService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tanqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
