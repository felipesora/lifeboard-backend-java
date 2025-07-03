package com.lifeboard.service;

import com.lifeboard.model.Transacao;
import com.lifeboard.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    public Page<Transacao> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Transacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com id: " + id));
    }

    public Transacao salvar(Transacao entity) {
        return repository.save(entity);
    }

    public Transacao atualizar(Long id, Transacao novaTransacao) {
        Transacao transacaoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com id: " + id));

        transacaoExistente.setDescricao(novaTransacao.getDescricao());
        transacaoExistente.setValor(novaTransacao.getValor());
        transacaoExistente.setTipo(novaTransacao.getTipo());
        transacaoExistente.setCategoria(novaTransacao.getCategoria());
        transacaoExistente.setFinanceiro(novaTransacao.getFinanceiro());

        return repository.save(transacaoExistente);
    }

    public String deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Transação deletada com sucesso!";
        }
        throw new RuntimeException("Erro ao deletar! Transação com " + id + " não encontrada.");
    }
}
