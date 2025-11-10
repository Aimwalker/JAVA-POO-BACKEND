package com.br.pdvpostocombustivel.api.venda;

import com.br.pdvpostocombustivel.api.venda.dto.ItemVendaFuncionarioResponse;
import com.br.pdvpostocombustivel.api.venda.dto.ItemVendaResponse;
import com.br.pdvpostocombustivel.api.venda.dto.VendaFuncionarioResponse;
import com.br.pdvpostocombustivel.api.venda.dto.VendaRequest;
import com.br.pdvpostocombustivel.api.venda.dto.VendaResponse;
import com.br.pdvpostocombustivel.domain.entity.*;
import com.br.pdvpostocombustivel.domain.repository.*;
import com.br.pdvpostocombustivel.enums.FormaPagamento;
import com.br.pdvpostocombustivel.enums.StatusCaixa;
import com.br.pdvpostocombustivel.enums.StatusVenda;
import com.br.pdvpostocombustivel.enums.TipoMovimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentoContaClienteRepository movimentoContaClienteRepository;
    private final CaixaRepository caixaRepository;

    public VendaService(VendaRepository vendaRepository, FuncionarioRepository funcionarioRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository, MovimentoContaClienteRepository movimentoContaClienteRepository, CaixaRepository caixaRepository) {
        this.vendaRepository = vendaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.movimentoContaClienteRepository = movimentoContaClienteRepository;
        this.caixaRepository = caixaRepository;
    }

    public VendaResponse create(VendaRequest req) {
        Funcionario funcionario = funcionarioRepository.findById(req.funcionarioId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o ID: " + req.funcionarioId()));

        Caixa caixaAberto = caixaRepository.findByStatus(StatusCaixa.ABERTO)
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto encontrado. Não é possível registrar vendas."));

        Venda novaVenda = new Venda();
        novaVenda.setFuncionario(funcionario);
        novaVenda.setData(LocalDateTime.now());
        novaVenda.setFormaPagamento(req.formaPagamento());
        novaVenda.setStatus(StatusVenda.FINALIZADA);
        novaVenda.setCaixa(caixaAberto);

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
            processarVendaContaClientePreSave(req, novaVenda, valorTotal);
        }

        Venda vendaSalva = vendaRepository.save(novaVenda);

        if (req.formaPagamento() == FormaPagamento.CONTA_CLIENTE) {
            criarMovimentoContaClientePostSave(vendaSalva, valorTotal);
        }

        return toResponse(vendaSalva);
    }

    // NOVO MÉTODO: LISTAR TODAS AS VENDAS (PAGINADO)
    @Transactional(readOnly = true)
    public Page<VendaResponse> listAll(Pageable pageable) {
        return vendaRepository.findAll(pageable).map(this::toResponse);
    }

    // MÉTODO: BUSCAR VENDAS POR FUNCIONÁRIO
    @Transactional(readOnly = true)
    public List<VendaFuncionarioResponse> getVendasByFuncionarioId(Long funcionarioId) {
        funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o ID: " + funcionarioId));

        List<Venda> vendas = vendaRepository.findByFuncionarioId(funcionarioId);
        return vendas.stream()
                .map(this::toVendaFuncionarioResponse)
                .collect(Collectors.toList());
    }

    // NOVO MÉTODO: EMITIR CUPOM FISCAL
    @Transactional(readOnly = true)
    public String emitirCupomFiscal(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada com o ID: " + vendaId));

        return gerarCupom(venda, "CUPOM FISCAL");
    }

    // MÉTODO: REEMITIR CUPOM FISCAL
    @Transactional(readOnly = true)
    public String reemitirCupomFiscal(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada com o ID: " + vendaId));

        return gerarCupom(venda, "CUPOM FISCAL - REEMISSÃO");
    }

    private String gerarCupom(Venda venda, String titulo) {
        StringBuilder cupom = new StringBuilder();
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("        %s        \n", titulo));
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("VENDA ID: %d\n", venda.getId()));
        cupom.append(String.format("DATA: %s\n", venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        cupom.append(String.format("FUNCIONÁRIO: %s\n", venda.getFuncionario().getNomeCompleto()));
        cupom.append(String.format("FORMA PGTO: %s\n", venda.getFormaPagamento()));
        if (venda.getCliente() != null) {
            cupom.append(String.format("CLIENTE: %s (ID: %d)\n", venda.getCliente().getNomeCompleto(), venda.getCliente().getId()));
        }
        cupom.append("----------------------------------------\n");
        cupom.append("ITENS:\n");

        BigDecimal totalImpostos = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {
            cupom.append(String.format("- %s (%.2f L/UN) x R$ %.2f = R$ %.2f\n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    item.getSubtotal()));

            // Calcula os impostos para o item
            if (item.getProduto().getAliquotaIcms() != null && item.getProduto().getAliquotaPisCofins() != null) {
                BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
                BigDecimal impostoIcms = item.getProduto().getAliquotaIcms().multiply(quantidade);
                BigDecimal impostoPisCofins = item.getProduto().getAliquotaPisCofins().multiply(quantidade);
                totalImpostos = totalImpostos.add(impostoIcms).add(impostoPisCofins);
            }
        }
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("TOTAL: R$ %.2f\n", venda.getValorTotal()));
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("Valor aprox. dos tributos: R$ %.2f (Fonte: IBPT)\n", totalImpostos));
        cupom.append("----------------------------------------\n");
        cupom.append("        OBRIGADO E VOLTE SEMPRE!        \n");
        cupom.append("----------------------------------------\n");

        return cupom.toString();
    }

    private void processarVendaContaClientePreSave(VendaRequest req, Venda novaVenda, BigDecimal valorTotal) {
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
        novaVenda.setCliente(cliente);
    }

    private void criarMovimentoContaClientePostSave(Venda vendaSalva, BigDecimal valorTotal) {
        ContaCliente conta = vendaSalva.getCliente().getContaCliente();
        MovimentoContaCliente movimento = new MovimentoContaCliente();
        movimento.setContaCliente(conta);
        movimento.setTipo(TipoMovimento.DEBITO);
        movimento.setValor(valorTotal);
        movimento.setDataHora(LocalDateTime.now());
        movimento.setDescricao("Venda ID: " + vendaSalva.getId());
        movimentoContaClienteRepository.save(movimento);
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
                venda.getCaixa().getId(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                itensResponse
        );
    }

    // Helper para converter Venda para VendaFuncionarioResponse
    private VendaFuncionarioResponse toVendaFuncionarioResponse(Venda venda) {
        List<ItemVendaFuncionarioResponse> itensResponse = venda.getItens().stream()
                .map(item -> new ItemVendaFuncionarioResponse(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                )).collect(Collectors.toList());

        return new VendaFuncionarioResponse(
                venda.getId(),
                venda.getData(),
                venda.getValorTotal(),
                venda.getFuncionario().getId(),
                venda.getFuncionario().getNomeCompleto(),
                venda.getCliente() != null ? venda.getCliente().getId() : null,
                venda.getCliente() != null ? venda.getCliente().getNomeCompleto() : null,
                venda.getCaixa().getId(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                itensResponse
        );
    }
}
