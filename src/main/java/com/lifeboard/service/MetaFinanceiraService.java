package com.lifeboard.service;

import com.lifeboard.exception.RegraNegocioException;
import com.lifeboard.exception.SaldoInsuficienteException;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.model.Transacao;
import com.lifeboard.model.enums.CategoriaTransacao;
import com.lifeboard.model.enums.StatusMeta;
import com.lifeboard.model.enums.TipoTransacao;
import com.lifeboard.repository.FinanceiroRepository;
import com.lifeboard.repository.MetaFinanceiraRepository;
import com.lifeboard.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class MetaFinanceiraService {

    @Autowired
    private MetaFinanceiraRepository metaRepository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Page<MetaFinanceira> listarTodos(Pageable pageable) {
        return metaRepository.findAllByOrderByIdAsc(pageable);
    }

    public MetaFinanceira buscarPorId(Long id) {
        return metaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira não encontrada com id: " + id));
    }

    @Transactional
    public MetaFinanceira salvar(MetaFinanceira entity) {
        Financeiro financeiro = financeiroRepository.findById(entity.getFinanceiro().getId())
                .orElseThrow(() -> new EntityNotFoundException("Financeiro não encontrado com id: " + entity.getFinanceiro().getId()));

        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorAtual = entity.getValorAtual();

        if (saldoAtual.compareTo(valorAtual) < 0) {
            throw new EntityNotFoundException("Saldo insuficiente para criar esta Meta Financeira!");
        }

        financeiro.setSaldoAtual(saldoAtual.subtract(valorAtual));
        financeiroRepository.save(financeiro);

        entity.setFinanceiro(financeiro);

        if (entity.getValorAtual().compareTo(entity.getValorMeta()) >= 0) {
            entity.setStatus(StatusMeta.CONCLUIDA);
        } else {
            entity.setStatus(StatusMeta.EM_ANDAMENTO);
        }
        return metaRepository.save(entity);
    }

    @Transactional
    public void adicionarSaldo(Long metaId, BigDecimal valor) {
        MetaFinanceira meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira não encontrada"));

        meta.setValorAtual(meta.getValorAtual().add(valor));
        metaRepository.save(meta);

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoFinanceiro = financeiro.getSaldoAtual();

        if (saldoFinanceiro.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a adição de saldo à meta financeira!");
        }

        Transacao transacao = new Transacao();
        transacao.setDescricao("Aplicação na meta: " + meta.getNome());
        transacao.setValor(valor);
        transacao.setTipo(TipoTransacao.APLICACAO);
        transacao.setCategoria(CategoriaTransacao.INVESTIMENTO);
        transacao.setFinanceiro(financeiro);
        transacaoRepository.save(transacao);

        financeiro.setSaldoAtual(saldoFinanceiro.subtract(valor));
        financeiroRepository.save(financeiro);
    }

    @Transactional
    public void retirarSaldo(Long metaId, BigDecimal valor) {
        MetaFinanceira meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira não encontrada"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O valor a ser retirado deve ser maior que zero.");
        }

        if (meta.getValorAtual().compareTo(valor) < 0) {
            throw new RegraNegocioException("Saldo insuficiente na meta.");
        }

        meta.setValorAtual(meta.getValorAtual().subtract(valor));
        metaRepository.save(meta);

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoFinanceiro = financeiro.getSaldoAtual();

        Transacao transacao = new Transacao();
        transacao.setDescricao("Retirada da meta: " + meta.getNome());
        transacao.setValor(valor);
        transacao.setTipo(TipoTransacao.RESGATE);
        transacao.setCategoria(CategoriaTransacao.INVESTIMENTO);
        transacao.setFinanceiro(financeiro);
        transacaoRepository.save(transacao);

        financeiro.setSaldoAtual(saldoFinanceiro.add(valor));
        financeiroRepository.save(financeiro);
    }

    @Transactional
    public MetaFinanceira atualizar(Long id, MetaFinanceira novaMeta) {
        MetaFinanceira metaExistente = metaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira não encontrada com id: " + id));

        String nomeAntigo = metaExistente.getNome();
        String nomeNovo = novaMeta.getNome();

        metaExistente.setNome(novaMeta.getNome());
        metaExistente.setValorMeta(novaMeta.getValorMeta());
        metaExistente.setDataLimite(novaMeta.getDataLimite());
        if (metaExistente.getValorAtual().compareTo(metaExistente.getValorMeta()) >= 0) {
            metaExistente.setStatus(StatusMeta.CONCLUIDA);
        } else {
            metaExistente.setStatus(StatusMeta.EM_ANDAMENTO);
        }

        // Atualizar descrições das transações relacionadas
        List<Transacao> transacoes = transacaoRepository.findByFinanceiro(metaExistente.getFinanceiro());
        for (Transacao t : transacoes) {
            if (t.getDescricao().equals("Aplicação na meta: " + nomeAntigo)) {
                t.setDescricao("Aplicação na meta: " + nomeNovo);
            } else if (t.getDescricao().equals("Retirada da meta: " + nomeAntigo)) {
                t.setDescricao("Retirada da meta: " + nomeNovo);
            }
        }
        transacaoRepository.saveAll(transacoes);

        return metaRepository.save(metaExistente);
    }

    @Transactional
    public String deletar(Long id) {
        MetaFinanceira meta = metaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira não encontrada com id: " + id));

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorAtual = meta.getValorAtual();

        // Devolve valor guardado na meta ao saldo
        financeiro.setSaldoAtual(saldoAtual.add(valorAtual));
        financeiroRepository.save(financeiro);

        // Buscar transações relacionadas com a meta (pela descrição)
        List<Transacao> transacoesRelacionadas = transacaoRepository.findByFinanceiro(financeiro)
                .stream()
                .filter(t -> t.getDescricao().contains(meta.getNome()))
                .toList();

        // Deletar as transações relacionadas
        transacaoRepository.deleteAll(transacoesRelacionadas);

        metaRepository.deleteById(id);
        return "Meta Financeira deletada com sucesso!";
    }
}
