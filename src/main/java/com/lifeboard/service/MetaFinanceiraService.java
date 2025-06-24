package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.repository.MetaFinanceiraRepository;

import java.util.List;

public class MetaFinanceiraService extends BaseServiceImpl<MetaFinanceira, Long, MetaFinanceiraRepository> {

    public MetaFinanceiraService(MetaFinanceiraRepository repository) {
        super(repository);
    }

    @Override
    public List<MetaFinanceira> listarTodos() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public MetaFinanceira atualizar(Long id, MetaFinanceira novaMeta) {
        MetaFinanceira metaExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meta Financeira não encontrada"));

        metaExistente.setNome(novaMeta.getNome());
        metaExistente.setValorMeta(novaMeta.getValorMeta());
        metaExistente.setDataLimite(novaMeta.getDataLimite());
        metaExistente.setStatus(novaMeta.getStatus());
        metaExistente.setFinanceiro(novaMeta.getFinanceiro());

        return repository.save(metaExistente);
    }
}
