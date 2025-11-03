package com.br.pdvpostocombustivel.api.cliente;

import com.br.pdvpostocombustivel.api.cliente.dto.ClienteRequest;
import com.br.pdvpostocombustivel.api.cliente.dto.ClienteResponse;
import com.br.pdvpostocombustivel.api.cliente.dto.PagamentoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> list(@PageableDefault(size = 10, sort = "nomeCompleto") Pageable pageable) {
        Page<ClienteResponse> page = clienteService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) {
        ClienteResponse response = clienteService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(@PathVariable Long id, @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<ClienteResponse> pagarConta(@PathVariable Long id, @RequestBody PagamentoRequest request) {
        ClienteResponse response = clienteService.pagarConta(id, request);
        return ResponseEntity.ok(response);
    }
}
