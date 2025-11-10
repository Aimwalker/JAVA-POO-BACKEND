package com.br.pdvpostocombustivel.domain.repository;

import com.br.pdvpostocombustivel.domain.entity.Bomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BombaRepository extends JpaRepository<Bomba, Long> {

    boolean existsByNumeroBombaFisicaAndNumeroBico(Integer numeroBombaFisica, Integer numeroBico);
}
