package com.br.pdvpostocombustivel.api.abastecimento;

import com.br.pdvpostocombustivel.api.abastecimento.dto.AbastecimentoRequest;
import com.br.pdvpostocombustivel.api.abastecimento.dto.AbastecimentoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/abastecimentos")
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    public AbastecimentoController(AbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @PostMapping
    public ResponseEntity<AbastecimentoResponse> create(@RequestBody AbastecimentoRequest request) {
        AbastecimentoResponse response = abastecimentoService.create(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
