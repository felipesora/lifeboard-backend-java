package com.lifeboard.repository;

import com.lifeboard.model.MetaFinanceira;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    Page<MetaFinanceira> findAllByOrderByIdAsc(Pageable pageable);
}
