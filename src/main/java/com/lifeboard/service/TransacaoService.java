package com.lifeboard.service;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Transacao;
import com.lifeboard.model.enums.TipoTransacao;
import com.lifeboard.repository.FinanceiroRepository;
import com.lifeboard.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    public Page<Transacao> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Transacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com id: " + id));
    }

    @Transactional
    public Transacao salvar(Transacao entity) {
        Financeiro financeiro = financeiroRepository.findById(entity.getFinanceiro().getId())
                .orElseThrow(() -> new RuntimeException("Financeiro não encontrado com id: " + entity.getFinanceiro().getId()));

        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorTransacao = entity.getValor();

        if (entity.getTipo() == TipoTransacao.SAIDA) {
            // Validar o saldo suficiente
            if (saldoAtual.compareTo(valorTransacao) < 0) {
                throw new RuntimeException("Saldo insuficiente para realizar a transação de SAIDA!");
            }

            // Subtrai do saldo
            financeiro.setSaldoAtual(saldoAtual.subtract(valorTransacao));
        } else if (entity.getTipo() == TipoTransacao.ENTRADA) {
            // Soma ao saldo
            financeiro.setSaldoAtual(saldoAtual.add(valorTransacao));
        }

        // Persiste atualização do saldo no financeiro
        financeiroRepository.save(financeiro);

        entity.setFinanceiro(financeiro);
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
