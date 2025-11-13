package com.br.pdvpostocombustivel.api.funcionario;

import com.br.pdvpostocombustivel.api.funcionario.dto.FuncionarioRequest;
import com.br.pdvpostocombustivel.api.funcionario.dto.FuncionarioResponse;
import com.br.pdvpostocombustivel.api.funcionario.dto.RegistroRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // NOVO ENDPOINT PÚBLICO PARA REGISTRO
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarNovoUsuario(@RequestBody RegistroRequest request) {
        funcionarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponse> create(@RequestBody FuncionarioRequest request) {
        FuncionarioResponse response = funcionarioService.create(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<FuncionarioResponse>> list(@PageableDefault(size = 10, sort = "nomeCompleto") Pageable pageable) {
        Page<FuncionarioResponse> page = funcionarioService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> getById(@PathVariable Long id) {
        FuncionarioResponse response = funcionarioService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> update(@PathVariable Long id, @RequestBody FuncionarioRequest request) {
        FuncionarioResponse response = funcionarioService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
