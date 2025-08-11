package com.lifeboard.service;

import com.lifeboard.dto.MetaFinanceiraResponseDTO;
import com.lifeboard.exception.BadRequestException;
import com.lifeboard.mapper.MetaFinanceiraMapper;
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
    private FinanceiroService financeiroService;

    @Autowired
    private TransacaoService transacaoService;

    public Page<MetaFinanceiraResponseDTO> listarTodos(Pageable pageable) {
        return metaRepository.findAllByOrderByIdAsc(pageable)
                .map(MetaFinanceiraMapper::toDTO);
    }

    public MetaFinanceiraResponseDTO buscarDTOPorId(Long id) {
        var meta = buscarEntidadePorId(id);

        return MetaFinanceiraMapper.toDTO(meta);
    }

    @Transactional
    public MetaFinanceiraResponseDTO salvar(MetaFinanceira metaFinanceira) {
        Financeiro financeiro = financeiroService.buscarEntidadePorId(metaFinanceira.getFinanceiro().getId());

        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorMetaAtual = metaFinanceira.getValorAtual();

        if (saldoAtual.compareTo(valorMetaAtual) < 0) {
            throw new BadRequestException("Saldo insuficiente para criar esta Meta Financeira!");
        }

        financeiro.setSaldoAtual(saldoAtual.subtract(valorMetaAtual));
        financeiroService.atualizar(financeiro.getId(), financeiro);

        metaFinanceira.setFinanceiro(financeiro);

        definirStatusMetaFinanceiro(metaFinanceira);

        var metaFinanceiraSalva = metaRepository.save(metaFinanceira);
        return MetaFinanceiraMapper.toDTO(metaFinanceira);
    }

    @Transactional
    public void adicionarSaldo(Long metaId, BigDecimal valor) {
        MetaFinanceira meta = buscarEntidadePorId(metaId);

        meta.setValorAtual(meta.getValorAtual().add(valor));
        metaRepository.save(meta);

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoFinanceiro = financeiro.getSaldoAtual();

        if (saldoFinanceiro.compareTo(valor) < 0) {
            throw new BadRequestException("Saldo insuficiente para realizar a adição de saldo à meta financeira!");
        }

        Transacao transacao = new Transacao();
        transacao.setDescricao("Aplicação na meta: " + meta.getNome());
        transacao.setValor(valor);
        transacao.setTipo(TipoTransacao.APLICACAO);
        transacao.setCategoria(CategoriaTransacao.INVESTIMENTO);
        transacao.setFinanceiro(financeiro);
        transacaoService.salvar(transacao);

        financeiro.setSaldoAtual(saldoFinanceiro.subtract(valor));
        financeiroService.atualizar(financeiro.getId(), financeiro);
    }

    @Transactional
    public void retirarSaldo(Long metaId, BigDecimal valor) {
        MetaFinanceira meta = buscarEntidadePorId(metaId);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("O valor a ser retirado deve ser maior que zero.");
        }

        if (meta.getValorAtual().compareTo(valor) < 0) {
            throw new BadRequestException("Saldo insuficiente na meta.");
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
        transacaoService.salvar(transacao);

        financeiro.setSaldoAtual(saldoFinanceiro.add(valor));
        financeiroService.atualizar(financeiro.getId(), financeiro);
    }

    @Transactional
    public MetaFinanceiraResponseDTO atualizar(Long id, MetaFinanceira novaMeta) {
        MetaFinanceira metaExistente = buscarEntidadePorId(id);

        String nomeMetaAntigo = metaExistente.getNome();
        String nomeMetaNovo = novaMeta.getNome();

        metaExistente.setNome(novaMeta.getNome());
        metaExistente.setValorMeta(novaMeta.getValorMeta());
        metaExistente.setDataLimite(novaMeta.getDataLimite());
        definirStatusMetaFinanceiro(metaExistente);

        // Atualizar descrições das transações relacionadas
        List<Transacao> transacoes = transacaoService.buscarTransacoesPorFinanceiro(metaExistente.getFinanceiro());
        for (Transacao t : transacoes) {
            if (t.getDescricao().equals("Aplicação na meta: " + nomeMetaAntigo)) {
                t.setDescricao("Aplicação na meta: " + nomeMetaNovo);
            } else if (t.getDescricao().equals("Retirada da meta: " + nomeMetaAntigo)) {
                t.setDescricao("Retirada da meta: " + nomeMetaNovo);
            }
        }
        transacaoService.atualizarVariasTransacoes(transacoes);

        var metaAtualizada = metaRepository.save(metaExistente);

        return MetaFinanceiraMapper.toDTO(metaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        MetaFinanceira meta = buscarEntidadePorId(id);

        Financeiro financeiro = meta.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorMetaAtual = meta.getValorAtual();

        financeiro.setSaldoAtual(saldoAtual.add(valorMetaAtual));
        financeiroService.atualizar(financeiro.getId(), financeiro);

        // Buscar transações relacionadas com a meta (pela descrição)
        List<Transacao> transacoesRelacionadas = transacaoService.buscarTransacoesPorFinanceiro(financeiro)
                .stream()
                .filter(t -> t.getDescricao().contains(meta.getNome()))
                .toList();

        // Deletar as transações relacionadas
        transacaoService.deletarVariasTransacoes(transacoesRelacionadas);

        metaRepository.deleteById(id);
    }

    public MetaFinanceira buscarEntidadePorId(Long id) {
        return metaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta Financeira com id: " + id + " não encontrada"));
    }

    public void definirStatusMetaFinanceiro(MetaFinanceira metaFinanceira) {

        if (metaFinanceira.getValorAtual().compareTo(metaFinanceira.getValorMeta()) >= 0) {
            metaFinanceira.setStatus(StatusMeta.CONCLUIDA);

        } else {
            metaFinanceira.setStatus(StatusMeta.EM_ANDAMENTO);
        }
    }
}
