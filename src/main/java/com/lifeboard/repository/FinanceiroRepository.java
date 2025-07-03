package com.lifeboard.repository;

import com.lifeboard.model.Financeiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {

    Page<Financeiro> findAllByOrderByIdAsc(Pageable pageable);
}
