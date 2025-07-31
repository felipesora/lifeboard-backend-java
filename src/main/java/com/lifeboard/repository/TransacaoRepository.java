package com.lifeboard.repository;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findAllByOrderByIdAsc(Pageable pageable);

    List<Transacao> findByFinanceiro(Financeiro financeiro);
}
