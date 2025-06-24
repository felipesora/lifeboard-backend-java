package com.lifeboard.repository;

import com.lifeboard.model.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    List<MetaFinanceira> findAllByOrderByIdAsc();
}
