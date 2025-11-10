package com.br.pdvpostocombustivel.api.venda;

import com.br.pdvpostocombustivel.api.venda.dto.VendaFuncionarioResponse;
import com.br.pdvpostocombustivel.api.venda.dto.VendaRequest;
import com.br.pdvpostocombustivel.api.venda.dto.VendaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    // NOVO ENDPOINT: Listar todas as vendas (paginado)
    @GetMapping
    public ResponseEntity<Page<VendaResponse>> listAll(@PageableDefault(size = 10, sort = "data") Pageable pageable) {
        Page<VendaResponse> page = vendaService.listAll(pageable);
        return ResponseEntity.ok(page);
    }

    // ENDPOINT: Buscar Vendas por Funcionário
    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<VendaFuncionarioResponse>> getVendasByFuncionarioId(@PathVariable Long funcionarioId) {
        List<VendaFuncionarioResponse> vendas = vendaService.getVendasByFuncionarioId(funcionarioId);
        return ResponseEntity.ok(vendas);
    }

    // NOVO ENDPOINT: Emissão de Cupom Fiscal
    @GetMapping("/{id}/emitir-cupom")
    public ResponseEntity<String> emitirCupom(@PathVariable Long id) {
        String cupom = vendaService.emitirCupomFiscal(id);
        return ResponseEntity.ok(cupom);
    }

    // ENDPOINT: Reemissão de Cupom Fiscal
    @GetMapping("/{id}/reemitir-cupom")
    public ResponseEntity<String> reemitirCupom(@PathVariable Long id) {
        String cupom = vendaService.reemitirCupomFiscal(id);
        return ResponseEntity.ok(cupom);
    }
}
