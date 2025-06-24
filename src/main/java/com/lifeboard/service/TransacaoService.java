package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.model.Transacao;
import com.lifeboard.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService extends BaseServiceImpl<Transacao, Long, TransacaoRepository> {

    public TransacaoService(TransacaoRepository repository) {
        super(repository);
    }

    @Override
    public List<Transacao> listarTodos() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Transacao atualizar(Long id, Transacao novaTransacao) {
        Transacao transacaoExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        transacaoExistente.setDescricao(novaTransacao.getDescricao());
        transacaoExistente.setValor(novaTransacao.getValor());
        transacaoExistente.setTipo(novaTransacao.getTipo());
        transacaoExistente.setData(novaTransacao.getData());
        transacaoExistente.setCategoria(novaTransacao.getCategoria());
        transacaoExistente.setFinanceiro(novaTransacao.getFinanceiro());

        return repository.save(transacaoExistente);
    }
}
