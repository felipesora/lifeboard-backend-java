package com.lifeboard.service;

import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.repository.MetaFinanceiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MetaFinanceiraService {

    @Autowired
    private MetaFinanceiraRepository repository;

    public Page<MetaFinanceira> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public MetaFinanceira buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta Financeira não encontrada com id: " + id));
    }

    public MetaFinanceira salvar(MetaFinanceira entity) {
        return repository.save(entity);
    }

    public MetaFinanceira atualizar(Long id, MetaFinanceira novaMeta) {
        MetaFinanceira metaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta Financeira não encontrada com id: " + id));

        metaExistente.setNome(novaMeta.getNome());
        metaExistente.setValorMeta(novaMeta.getValorMeta());
        metaExistente.setDataLimite(novaMeta.getDataLimite());
        metaExistente.setStatus(novaMeta.getStatus());
        metaExistente.setFinanceiro(novaMeta.getFinanceiro());

        return repository.save(metaExistente);
    }

    public String deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Meta Financeira deletada com sucesso!";
        }
        return "Erro ao deletar! Meta Financeira com " + id + " não encontrada.";
    }
}
