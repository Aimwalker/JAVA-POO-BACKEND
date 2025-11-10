package com.br.pdvpostocombustivel.config;

import com.br.pdvpostocombustivel.domain.entity.*;
import com.br.pdvpostocombustivel.domain.repository.*;
import com.br.pdvpostocombustivel.enums.StatusBomba;
import com.br.pdvpostocombustivel.enums.StatusCaixa;
import com.br.pdvpostocombustivel.enums.TipoPessoa;
import com.br.pdvpostocombustivel.enums.TipoProduto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final AcessoRepository acessoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final TanqueRepository tanqueRepository;
    private final BombaRepository bombaRepository;
    private final CaixaRepository caixaRepository;

    public DataInitializer(AcessoRepository acessoRepository, FuncionarioRepository funcionarioRepository, ProdutoRepository produtoRepository, TanqueRepository tanqueRepository, BombaRepository bombaRepository, CaixaRepository caixaRepository) {
        this.acessoRepository = acessoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.tanqueRepository = tanqueRepository;
        this.bombaRepository = bombaRepository;
        this.caixaRepository = caixaRepository;
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
            // Combustíveis
            Produto gasolinaComum = new Produto();
            gasolinaComum.setNome("Gasolina Comum");
            gasolinaComum.setCodigoBarras("789000000001");
            gasolinaComum.setPrecoVenda(new BigDecimal("5.59"));
            gasolinaComum.setTipo(TipoProduto.COMBUSTIVEL);
            gasolinaComum.setEstoqueAtual(20000.0);
            gasolinaComum.setAliquotaIcms(new BigDecimal("1.3721"));
            gasolinaComum.setAliquotaPisCofins(new BigDecimal("0.7925"));
            produtoRepository.save(gasolinaComum);

            Produto gasolinaAditivada = new Produto();
            gasolinaAditivada.setNome("Gasolina Aditivada");
            gasolinaAditivada.setCodigoBarras("789000000003");
            gasolinaAditivada.setPrecoVenda(new BigDecimal("5.79"));
            gasolinaAditivada.setTipo(TipoProduto.COMBUSTIVEL);
            gasolinaAditivada.setEstoqueAtual(15000.0);
            gasolinaAditivada.setAliquotaIcms(new BigDecimal("1.3721"));
            gasolinaAditivada.setAliquotaPisCofins(new BigDecimal("0.7925"));
            produtoRepository.save(gasolinaAditivada);

            Produto etanolComum = new Produto();
            etanolComum.setNome("Etanol Comum");
            etanolComum.setCodigoBarras("789000000004");
            etanolComum.setPrecoVenda(new BigDecimal("3.99"));
            etanolComum.setTipo(TipoProduto.COMBUSTIVEL);
            etanolComum.setEstoqueAtual(18000.0);
            etanolComum.setAliquotaIcms(new BigDecimal("1.3721"));
            etanolComum.setAliquotaPisCofins(new BigDecimal("0.1339"));
            produtoRepository.save(etanolComum);

            Produto dieselS500 = new Produto();
            dieselS500.setNome("Diesel S500");
            dieselS500.setCodigoBarras("789000000005");
            dieselS500.setPrecoVenda(new BigDecimal("4.89"));
            dieselS500.setTipo(TipoProduto.COMBUSTIVEL);
            dieselS500.setEstoqueAtual(25000.0);
            dieselS500.setAliquotaIcms(new BigDecimal("1.06")); // Valor aproximado para Diesel
            dieselS500.setAliquotaPisCofins(new BigDecimal("0.35")); // Valor aproximado para Diesel
            produtoRepository.save(dieselS500);

            Produto dieselS10 = new Produto();
            dieselS10.setNome("Diesel S10");
            dieselS10.setCodigoBarras("789000000006");
            dieselS10.setPrecoVenda(new BigDecimal("5.09"));
            dieselS10.setTipo(TipoProduto.COMBUSTIVEL);
            dieselS10.setEstoqueAtual(22000.0);
            dieselS10.setAliquotaIcms(new BigDecimal("1.06")); // Valor aproximado para Diesel
            dieselS10.setAliquotaPisCofins(new BigDecimal("0.35")); // Valor aproximado para Diesel
            produtoRepository.save(dieselS10);

            // Conveniência
            Produto agua = new Produto();
            agua.setNome("Água Mineral 500ml");
            agua.setCodigoBarras("789000000002");
            agua.setPrecoVenda(new BigDecimal("3.00"));
            agua.setTipo(TipoProduto.CONVENIENCIA);
            agua.setEstoqueAtual(100.0);
            produtoRepository.save(agua);
        }

        if (tanqueRepository.count() == 0) {
            Produto gasolinaComum = produtoRepository.findByCodigoBarras("789000000001").orElseThrow();
            Produto gasolinaAditivada = produtoRepository.findByCodigoBarras("789000000003").orElseThrow();
            Produto etanolComum = produtoRepository.findByCodigoBarras("789000000004").orElseThrow();
            Produto dieselS500 = produtoRepository.findByCodigoBarras("789000000005").orElseThrow();
            Produto dieselS10 = produtoRepository.findByCodigoBarras("789000000006").orElseThrow();

            Tanque tanqueGasolinaComum = new Tanque();
            tanqueGasolinaComum.setCombustivel(gasolinaComum);
            tanqueGasolinaComum.setCapacidade(30000.0);
            tanqueGasolinaComum.setNivelAtual(gasolinaComum.getEstoqueAtual());
            tanqueRepository.save(tanqueGasolinaComum);

            Tanque tanqueGasolinaAditivada = new Tanque();
            tanqueGasolinaAditivada.setCombustivel(gasolinaAditivada);
            tanqueGasolinaAditivada.setCapacidade(25000.0);
            tanqueGasolinaAditivada.setNivelAtual(gasolinaAditivada.getEstoqueAtual());
            tanqueRepository.save(tanqueGasolinaAditivada);

            Tanque tanqueEtanolComum = new Tanque();
            tanqueEtanolComum.setCombustivel(etanolComum);
            tanqueEtanolComum.setCapacidade(28000.0);
            tanqueEtanolComum.setNivelAtual(etanolComum.getEstoqueAtual());
            tanqueRepository.save(tanqueEtanolComum);

            Tanque tanqueDieselS500 = new Tanque();
            tanqueDieselS500.setCombustivel(dieselS500);
            tanqueDieselS500.setCapacidade(35000.0);
            tanqueDieselS500.setNivelAtual(dieselS500.getEstoqueAtual());
            tanqueRepository.save(tanqueDieselS500);

            Tanque tanqueDieselS10 = new Tanque();
            tanqueDieselS10.setCombustivel(dieselS10);
            tanqueDieselS10.setCapacidade(32000.0);
            tanqueDieselS10.setNivelAtual(dieselS10.getEstoqueAtual());
            tanqueRepository.save(tanqueDieselS10);
        }

        if (bombaRepository.count() == 0) {
            // Buscar tanques criados
            Tanque tanqueGasolinaComum = tanqueRepository.findByCombustivelNome("Gasolina Comum").orElseThrow();
            Tanque tanqueGasolinaAditivada = tanqueRepository.findByCombustivelNome("Gasolina Aditivada").orElseThrow();
            Tanque tanqueEtanolComum = tanqueRepository.findByCombustivelNome("Etanol Comum").orElseThrow();
            Tanque tanqueDieselS500 = tanqueRepository.findByCombustivelNome("Diesel S500").orElseThrow();
            Tanque tanqueDieselS10 = tanqueRepository.findByCombustivelNome("Diesel S10").orElseThrow();

            // Pista Esquerda (Carros e Motos - Gasolina Comum, Aditivada, Etanol Comum)
            // Bomba Física 1 (Pista Esquerda)
            Bomba b1_bico1_GC = new Bomba();
            b1_bico1_GC.setNumeroBombaFisica(1);
            b1_bico1_GC.setNumeroBico(1);
            b1_bico1_GC.setTanque(tanqueGasolinaComum);
            b1_bico1_GC.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b1_bico1_GC);

            Bomba b1_bico2_GA = new Bomba();
            b1_bico2_GA.setNumeroBombaFisica(1);
            b1_bico2_GA.setNumeroBico(2);
            b1_bico2_GA.setTanque(tanqueGasolinaAditivada);
            b1_bico2_GA.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b1_bico2_GA);

            Bomba b1_bico3_ET = new Bomba();
            b1_bico3_ET.setNumeroBombaFisica(1);
            b1_bico3_ET.setNumeroBico(3);
            b1_bico3_ET.setTanque(tanqueEtanolComum);
            b1_bico3_ET.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b1_bico3_ET);

            // Bomba Física 2 (Pista Esquerda)
            Bomba b2_bico1_GC = new Bomba();
            b2_bico1_GC.setNumeroBombaFisica(2);
            b2_bico1_GC.setNumeroBico(1);
            b2_bico1_GC.setTanque(tanqueGasolinaComum);
            b2_bico1_GC.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b2_bico1_GC);

            Bomba b2_bico2_GA = new Bomba();
            b2_bico2_GA.setNumeroBombaFisica(2);
            b2_bico2_GA.setNumeroBico(2);
            b2_bico2_GA.setTanque(tanqueGasolinaAditivada);
            b2_bico2_GA.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b2_bico2_GA);

            Bomba b2_bico3_ET = new Bomba();
            b2_bico3_ET.setNumeroBombaFisica(2);
            b2_bico3_ET.setNumeroBico(3);
            b2_bico3_ET.setTanque(tanqueEtanolComum);
            b2_bico3_ET.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b2_bico3_ET);

            // Pista Direita (2 Bombas para Gasolina/Etanol, 2 Bombas para Diesel)
            // Bomba Física 3 (Pista Direita - Gasolina/Etanol)
            Bomba b3_bico1_GC = new Bomba();
            b3_bico1_GC.setNumeroBombaFisica(3);
            b3_bico1_GC.setNumeroBico(1);
            b3_bico1_GC.setTanque(tanqueGasolinaComum);
            b3_bico1_GC.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b3_bico1_GC);

            Bomba b3_bico2_GA = new Bomba();
            b3_bico2_GA.setNumeroBombaFisica(3);
            b3_bico2_GA.setNumeroBico(2);
            b3_bico2_GA.setTanque(tanqueGasolinaAditivada);
            b3_bico2_GA.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b3_bico2_GA);

            // Bomba Física 4 (Pista Direita - Diesel)
            Bomba b4_bico1_DS500 = new Bomba();
            b4_bico1_DS500.setNumeroBombaFisica(4);
            b4_bico1_DS500.setNumeroBico(1);
            b4_bico1_DS500.setTanque(tanqueDieselS500);
            b4_bico1_DS500.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b4_bico1_DS500);

            Bomba b4_bico2_DS10 = new Bomba();
            b4_bico2_DS10.setNumeroBombaFisica(4);
            b4_bico2_DS10.setNumeroBico(2);
            b4_bico2_DS10.setTanque(tanqueDieselS10);
            b4_bico2_DS10.setStatus(StatusBomba.ATIVA);
            bombaRepository.save(b4_bico2_DS10);
        }

        // 5. Cria um caixa padrão se não existir
        if (caixaRepository.findByStatus(StatusCaixa.ABERTO).isEmpty() && funcionarioRepository.count() > 0) {
            Funcionario gerente = funcionarioRepository.findByEmail("gerente@email.com").orElseThrow();
            Caixa caixa = new Caixa();
            caixa.setDataAbertura(LocalDateTime.now());
            caixa.setValorInicial(new BigDecimal("500.00"));
            caixa.setUsuarioAbertura(gerente);
            caixa.setStatus(StatusCaixa.ABERTO);
            caixaRepository.save(caixa);
        }
    }
}
