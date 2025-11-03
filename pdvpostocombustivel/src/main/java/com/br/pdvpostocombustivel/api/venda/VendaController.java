package com.br.pdvpostocombustivel.api.venda;

import com.br.pdvpostocombustivel.api.venda.dto.VendaRequest;
import com.br.pdvpostocombustivel.api.venda.dto.VendaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> create(@RequestBody VendaRequest vendaRequest) {
        VendaResponse vendaResponse = vendaService.create(vendaRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(vendaResponse.id()).toUri();

        return ResponseEntity.created(uri).body(vendaResponse);
    }
}
