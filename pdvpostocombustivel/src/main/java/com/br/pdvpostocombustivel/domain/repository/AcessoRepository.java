package com.br.pdvpostocombustivel.domain.repository;

import com.br.pdvpostocombustivel.domain.entity.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    Optional<Acesso> findByDescricao(String descricao);
}
