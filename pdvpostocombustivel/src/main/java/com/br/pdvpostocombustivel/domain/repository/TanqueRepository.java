package com.br.pdvpostocombustivel.domain.repository;

import com.br.pdvpostocombustivel.domain.entity.Tanque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TanqueRepository extends JpaRepository<Tanque, Long> {

    Optional<Tanque> findByCombustivelNome(String nome);
}
