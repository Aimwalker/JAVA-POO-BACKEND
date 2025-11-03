package com.br.pdvpostocombustivel.config;

import com.br.pdvpostocombustivel.domain.entity.*;
import com.br.pdvpostocombustivel.domain.repository.*;
import com.br.pdvpostocombustivel.enums.StatusBomba;
import com.br.pdvpostocombustivel.enums.TipoPessoa;
import com.br.pdvpostocombustivel.enums.TipoProduto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final AcessoRepository acessoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final TanqueRepository tanqueRepository;
    private final BombaRepository bombaRepository;

    public DataInitializer(AcessoRepository acessoRepository, FuncionarioRepository funcionarioRepository, ProdutoRepository produtoRepository, TanqueRepository tanqueRepository, BombaRepository bombaRepository) {
        this.acessoRepository = acessoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.tanqueRepository = tanqueRepository;
        this.bombaRepository = bombaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (acessoRepository.count() == 0) {
            Acesso acessoGerente = new Acesso();
            acessoGerente.setDescricao("ROLE_GERENTE");
            acessoRepository.save(acessoGerente);

            Acesso acessoFuncionario = new Acesso();
            acessoFuncionario.setDescricao("ROLE_FUNCIONARIO");
            acessoRepository.save(acessoFuncionario);
        }

        if (funcionarioRepository.count() == 0) {
            Acesso acessoGerente = acessoRepository.findByDescricao("ROLE_GERENTE").orElseThrow();
            Acesso acessoFuncionario = acessoRepository.findByDescricao("ROLE_FUNCIONARIO").orElseThrow();

            Funcionario gerente = new Funcionario();
            gerente.setNomeCompleto("Gerente Padrão");
            gerente.setCpfCnpj("00000000000");
            gerente.setEmail("gerente@email.com");
            gerente.setDataNascimento(LocalDate.of(1980, 1, 1));
            gerente.setTipoPessoa(TipoPessoa.FISICA);
            gerente.setSenha("123456");
            gerente.setAcessos(Set.of(acessoGerente, acessoFuncionario));
            funcionarioRepository.save(gerente);

            Funcionario funcionario = new Funcionario();
            funcionario.setNomeCompleto("Funcionário Padrão");
            funcionario.setCpfCnpj("11111111111");
            funcionario.setEmail("funcionario@email.com");
            funcionario.setDataNascimento(LocalDate.of(1990, 1, 1));
            funcionario.setTipoPessoa(TipoPessoa.FISICA);
            funcionario.setSenha("123456");
            funcionario.setAcessos(Set.of(acessoFuncionario));
            funcionarioRepository.save(funcionario);
        }

        if (produtoRepository.count() == 0) {
            Produto gasolina = new Produto();
            gasolina.setNome("Gasolina Comum");
            gasolina.setCodigoBarras("789000000001");
            gasolina.setPrecoVenda(new BigDecimal("5.59"));
            gasolina.setTipo(TipoProduto.COMBUSTIVEL);
            gasolina.setEstoqueAtual(10000.0);
            produtoRepository.save(gasolina);

            Produto agua = new Produto();
            agua.setNome("Água Mineral 500ml");
            agua.setCodigoBarras("789000000002");
            agua.setPrecoVenda(new BigDecimal("3.00"));
            agua.setTipo(TipoProduto.CONVENIENCIA);
            agua.setEstoqueAtual(100.0);
            produtoRepository.save(agua);
        }

        if (tanqueRepository.count() == 0) {
            Produto gasolina = produtoRepository.findByCodigoBarras("789000000001").orElseThrow();
            Tanque tanqueGasolina = new Tanque();
            tanqueGasolina.setCombustivel(gasolina);
            tanqueGasolina.setCapacidade(20000.0);
            tanqueGasolina.setNivelAtual(gasolina.getEstoqueAtual());
            tanqueRepository.save(tanqueGasolina);
        }

        if (bombaRepository.count() == 0) {
            Tanque tanqueGasolina = tanqueRepository.findById(1L).orElseThrow();
            Bomba bomba1 = new Bomba();
            bomba1.setNumero(1);
            bomba1.setTanque(tanqueGasolina);
            bomba1.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(bomba1);
        }
    }
}
