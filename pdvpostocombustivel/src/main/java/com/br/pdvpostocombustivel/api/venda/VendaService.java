package com.br.pdvpostocombustivel.api.venda;

import com.br.pdvpostocombustivel.api.venda.dto.ItemVendaResponse;
import com.br.pdvpostocombustivel.api.venda.dto.VendaRequest;
import com.br.pdvpostocombustivel.api.venda.dto.VendaResponse;
import com.br.pdvpostocombustivel.domain.entity.*;
import com.br.pdvpostocombustivel.domain.repository.*;
import com.br.pdvpostocombustivel.enums.FormaPagamento;
import com.br.pdvpostocombustivel.enums.StatusVenda;
import com.br.pdvpostocombustivel.enums.TipoMovimento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public VendaService(VendaRepository vendaRepository, FuncionarioRepository funcionarioRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.vendaRepository = vendaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    public VendaResponse create(VendaRequest req) {
        Funcionario funcionario = funcionarioRepository.findById(req.funcionarioId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o ID: " + req.funcionarioId()));

        Venda novaVenda = new Venda();
        novaVenda.setFuncionario(funcionario);
        novaVenda.setData(LocalDateTime.now());
        novaVenda.setFormaPagamento(req.formaPagamento());
        novaVenda.setStatus(StatusVenda.FINALIZADA);

        List<ItemVenda> itensVenda = req.itens().stream().map(itemReq -> {
            Produto produto = produtoRepository.findById(itemReq.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + itemReq.produtoId()));

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(itemReq.quantidade());
            itemVenda.setPrecoUnitario(produto.getPrecoVenda());
            itemVenda.setSubtotal(produto.getPrecoVenda().multiply(BigDecimal.valueOf(itemReq.quantidade())));
            novaVenda.addItem(itemVenda);
            return itemVenda;
        }).collect(Collectors.toList());

        BigDecimal valorTotal = itensVenda.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        novaVenda.setValorTotal(valorTotal);

        if (req.formaPagamento() == FormaPagamento.CONTA_CLIENTE) {
            processarVendaContaCliente(req, novaVenda, valorTotal);
        }

        Venda vendaSalva = vendaRepository.save(novaVenda);
        return toResponse(vendaSalva);
    }

    private void processarVendaContaCliente(VendaRequest req, Venda novaVenda, BigDecimal valorTotal) {
        if (req.clienteId() == null) {
            throw new IllegalArgumentException("O ID do cliente é obrigatório para vendas em conta.");
        }
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + req.clienteId()));

        ContaCliente conta = cliente.getContaCliente();
        BigDecimal saldoDevedor = conta.getSaldo();
        BigDecimal limiteDisponivel = cliente.getLimiteCredito().subtract(saldoDevedor);

        if (valorTotal.compareTo(limiteDisponivel) > 0) {
            throw new IllegalStateException("Limite de crédito insuficiente. Limite disponível: " + limiteDisponivel);
        }

        conta.setSaldo(saldoDevedor.add(valorTotal));

        MovimentoContaCliente movimento = new MovimentoContaCliente();
        movimento.setContaCliente(conta);
        movimento.setTipo(TipoMovimento.DEBITO);
        movimento.setValor(valorTotal);
        movimento.setDataHora(LocalDateTime.now());
        movimento.setDescricao("Venda ID: " + novaVenda.getId()); // O ID da venda será nulo aqui, mas o JPA preencherá após salvar.
        conta.getHistoricoTransacoes().add(movimento);

        novaVenda.setCliente(cliente);
    }

    private VendaResponse toResponse(Venda venda) {
        List<ItemVendaResponse> itensResponse = venda.getItens().stream()
                .map(item -> new ItemVendaResponse(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                )).collect(Collectors.toList());

        return new VendaResponse(
                venda.getId(),
                venda.getData(),
                venda.getValorTotal(),
                venda.getFuncionario().getId(),
                venda.getFuncionario().getNomeCompleto(),
                venda.getCliente() != null ? venda.getCliente().getId() : null,
                venda.getCliente() != null ? venda.getCliente().getNomeCompleto() : null,
                venda.getFormaPagamento(),
                venda.getStatus(),
                itensResponse
        );
    }
}
