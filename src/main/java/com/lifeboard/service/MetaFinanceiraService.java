package com.lifeboard.service;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.model.enums.StatusMeta;
import com.lifeboard.repository.FinanceiroRepository;
import com.lifeboard.repository.MetaFinanceiraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class MetaFinanceiraService {

    @Autowired
    private MetaFinanceiraRepository repository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Page<MetaFinanceira> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public MetaFinanceira buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta Financeira não encontrada com id: " + id));
    }

    @Transactional
    public MetaFinanceira salvar(MetaFinanceira entity) {
        Financeiro financeiro = financeiroRepository.findById(entity.getFinanceiro().getId())
                .orElseThrow(() -> new RuntimeException("Financeiro não encontrado com id: " + entity.getFinanceiro().getId()));

        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorAtual = entity.getValorAtual();

        if (saldoAtual.compareTo(valorAtual) < 0) {
            throw new RuntimeException("Saldo insuficiente para criar esta Meta Financeira!");
        }

        financeiro.setSaldoAtual(saldoAtual.subtract(valorAtual));
        financeiroRepository.save(financeiro);

        entity.setFinanceiro(financeiro);

        if (entity.getValorAtual().compareTo(entity.getValorMeta()) >= 0) {
            entity.setStatus(StatusMeta.CONCLUIDA);
        } else {
            entity.setStatus(StatusMeta.EM_ANDAMENTO);
        }
        return repository.save(entity);
    }

    @Transactional
    public MetaFinanceira atualizar(Long id, MetaFinanceira novaMeta) {
        MetaFinanceira metaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta Financeira não encontrada com id: " + id));

        Financeiro financeiro = metaExistente.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();

        BigDecimal valorAntigo = metaExistente.getValorAtual();
        BigDecimal valorNovo = novaMeta.getValorAtual();

        // Desfaz o valor antigo: devolve ao saldo
        saldoAtual = saldoAtual.add(valorAntigo);

        // Verifica saldo suficiente para aplicar o novo valor
        if (saldoAtual.compareTo(valorNovo) < 0) {
            throw new RuntimeException("Saldo insuficiente para atualizar esta Meta Financeira!");
        }

        // Aplica o novo valor
        saldoAtual = saldoAtual.subtract(valorNovo);
        financeiro.setSaldoAtual(saldoAtual);
        financeiroRepository.save(financeiro);

        metaExistente.setNome(novaMeta.getNome());
        metaExistente.setValorMeta(novaMeta.getValorMeta());
        metaExistente.setValorAtual(valorNovo);
        metaExistente.setDataLimite(novaMeta.getDataLimite());
        if (metaExistente.getValorAtual().compareTo(metaExistente.getValorMeta()) >= 0) {
            metaExistente.setStatus(StatusMeta.CONCLUIDA);
        } else {
            metaExistente.setStatus(StatusMeta.EM_ANDAMENTO);
        }

        return repository.save(metaExistente);
    }

    @Transactional
    public String deletar(Long id) {
        MetaFinanceira meta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta Financeira não encontrada com id: " + id));

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorAtual = meta.getValorAtual();

        // Devolve valor guardado na meta ao saldo
        financeiro.setSaldoAtual(saldoAtual.add(valorAtual));
        financeiroRepository.save(financeiro);

        repository.deleteById(id);
        return "Meta Financeira deletada com sucesso!";
    }
}
