package com.lifeboard.repository;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    Page<MetaFinanceira> findAllByOrderByIdAsc(Pageable pageable);

    Optional<MetaFinanceira> findByFinanceiroAndNome(Financeiro financeiro, String nome);
}
