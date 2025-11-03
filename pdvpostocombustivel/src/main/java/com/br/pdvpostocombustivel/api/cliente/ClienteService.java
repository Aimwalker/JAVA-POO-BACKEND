package com.br.pdvpostocombustivel.api.cliente;

import com.br.pdvpostocombustivel.api.cliente.dto.ClienteRequest;
import com.br.pdvpostocombustivel.api.cliente.dto.ClienteResponse;
import com.br.pdvpostocombustivel.api.cliente.dto.PagamentoRequest;
import com.br.pdvpostocombustivel.domain.entity.Cliente;
import com.br.pdvpostocombustivel.domain.entity.ContaCliente;
import com.br.pdvpostocombustivel.domain.entity.MovimentoContaCliente;
import com.br.pdvpostocombustivel.domain.repository.ClienteRepository;
import com.br.pdvpostocombustivel.enums.TipoMovimento;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // CREATE
    @Transactional
    public ClienteResponse create(ClienteRequest req) {
        if (clienteRepository.existsByCpfCnpj(req.cpfCnpj())) {
            throw new DataIntegrityViolationException("CPF/CNPJ já cadastrado: " + req.cpfCnpj());
        }
        Cliente novoCliente = toEntity(new Cliente(), req);

        ContaCliente novaConta = new ContaCliente();
        novaConta.setCliente(novoCliente);
        novoCliente.setContaCliente(novaConta);

        Cliente clienteSalvo = clienteRepository.save(novoCliente);
        return toResponse(clienteSalvo);
    }

    // READ by ID
    public ClienteResponse getById(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado. id=" + id));
        return toResponse(c);
    }

    // LIST paginado
    public Page<ClienteResponse> list(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(this::toResponse);
    }

    // UPDATE
    @Transactional
    public ClienteResponse update(Long id, ClienteRequest req) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado. id=" + id));

        if (!c.getCpfCnpj().equals(req.cpfCnpj()) && clienteRepository.existsByCpfCnpj(req.cpfCnpj())) {
            throw new DataIntegrityViolationException("CPF/CNPJ já cadastrado: " + req.cpfCnpj());
        }

        Cliente clienteAtualizado = toEntity(c, req);
        clienteRepository.save(clienteAtualizado);
        return toResponse(clienteAtualizado);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado. id=" + id);
        }
        clienteRepository.deleteById(id);
    }

    // PAGAR CONTA
    @Transactional
    public ClienteResponse pagarConta(Long id, PagamentoRequest req) {
        if (req.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }

        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado. id=" + id));

        ContaCliente conta = c.getContaCliente();
        conta.setSaldo(conta.getSaldo().subtract(req.valor()));

        MovimentoContaCliente movimento = new MovimentoContaCliente();
        movimento.setContaCliente(conta);
        movimento.setTipo(TipoMovimento.CREDITO);
        movimento.setValor(req.valor());
        movimento.setDataHora(LocalDateTime.now());
        movimento.setDescricao("Pagamento recebido");
        conta.getHistoricoTransacoes().add(movimento);

        clienteRepository.save(c); // Salva o cliente, que por cascata salvará a conta e o movimento
        return toResponse(c);
    }

    // ---------- Helpers ----------

    private Cliente toEntity(Cliente c, ClienteRequest req) {
        c.setNomeCompleto(req.nomeCompleto());
        c.setCpfCnpj(req.cpfCnpj());
        c.setEmail(req.email());
        c.setDataNascimento(req.dataNascimento());
        c.setTipoPessoa(req.tipoPessoa());
        c.setLimiteCredito(req.limiteCredito());
        return c;
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNomeCompleto(),
                c.getCpfCnpj(),
                c.getEmail(),
                c.getDataNascimento(),
                c.getLimiteCredito(),
                c.getContaCliente() != null ? c.getContaCliente().getSaldo() : BigDecimal.ZERO
        );
    }
}
