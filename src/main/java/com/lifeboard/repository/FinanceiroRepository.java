package com.lifeboard.repository;

import com.lifeboard.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {

    List<Financeiro> findAllByOrderByIdAsc();
}
