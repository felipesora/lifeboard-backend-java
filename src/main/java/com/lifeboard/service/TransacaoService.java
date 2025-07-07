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

        Financeiro financeiro = transacaoExistente.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();

        BigDecimal valorAntigo = transacaoExistente.getValor();
        TipoTransacao tipoAntigo = transacaoExistente.getTipo();

        BigDecimal valorNovo = novaTransacao.getValor();
        TipoTransacao tipoNovo = novaTransacao.getTipo();

        // Desfaz o efeito antigo
        if (tipoAntigo == TipoTransacao.SAIDA) {
            saldoAtual = saldoAtual.add(valorAntigo);
        } else if (tipoAntigo == TipoTransacao.ENTRADA) {
            saldoAtual = saldoAtual.subtract(valorAntigo);
        }

        // Aplica o efeito novo com validação
        if (tipoNovo == TipoTransacao.SAIDA) {
            if (saldoAtual.compareTo(valorNovo) < 0) {
                throw new RuntimeException("Saldo insuficiente para realizar a atualização de SAIDA!");
            }
            saldoAtual = saldoAtual.subtract(valorNovo);
        } else if (tipoNovo == TipoTransacao.ENTRADA) {
            saldoAtual = saldoAtual.add(valorNovo);
        }

        // Atualiza o financeiro e transação
        financeiro.setSaldoAtual(saldoAtual);
        financeiroRepository.save(financeiro);

        transacaoExistente.setDescricao(novaTransacao.getDescricao());
        transacaoExistente.setValor(valorNovo);
        transacaoExistente.setTipo(tipoNovo);
        transacaoExistente.setCategoria(novaTransacao.getCategoria());

        return repository.save(transacaoExistente);
    }

    public String deletar(Long id) {
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com id: " + id));

        Financeiro financeiro = transacao.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valor = transacao.getValor();

        if (transacao.getTipo() == TipoTransacao.SAIDA) {
            // Desfazer SAIDA → soma de volta
            saldoAtual = saldoAtual.add(valor);
        } else if (transacao.getTipo() == TipoTransacao.ENTRADA) {
            // Desfazer ENTRADA → subtrai
            if (saldoAtual.compareTo(valor) < 0) {
                throw new RuntimeException("Saldo insuficiente para remover esta ENTRADA. Isso deixaria o saldo negativo!");
            }
            saldoAtual = saldoAtual.subtract(valor);
        }

        financeiro.setSaldoAtual(saldoAtual);
        financeiroRepository.save(financeiro);

        repository.deleteById(id);

        return "Transação deletada com sucesso!";
    }
}
