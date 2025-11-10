package com.br.pdvpostocombustivel.api.caixa;

import com.br.pdvpostocombustivel.api.caixa.dto.CaixaAberturaRequest;
import com.br.pdvpostocombustivel.api.caixa.dto.CaixaFechamentoRequest;
import com.br.pdvpostocombustivel.api.caixa.dto.CaixaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/caixas")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<CaixaResponse> abrirCaixa(@RequestBody CaixaAberturaRequest request) {
        CaixaResponse response = caixaService.abrirCaixa(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<CaixaResponse> fecharCaixa(@PathVariable Long id, @RequestBody CaixaFechamentoRequest request) {
        CaixaResponse response = caixaService.fecharCaixa(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aberto")
    public ResponseEntity<CaixaResponse> buscarCaixaAberto() {
        CaixaResponse response = caixaService.buscarCaixaAberto();
        return ResponseEntity.ok(response);
    }
}
