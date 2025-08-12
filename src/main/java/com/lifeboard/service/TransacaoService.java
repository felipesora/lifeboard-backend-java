package com.lifeboard.service;

import com.lifeboard.dto.TransacaoResponseDTO;
import com.lifeboard.mapper.TransacaoMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.model.Transacao;
import com.lifeboard.model.enums.TipoTransacao;
import com.lifeboard.repository.MetaFinanceiraRepository;
import com.lifeboard.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lifeboard.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private MetaFinanceiraRepository metaFinanceiraRepository;

    public Page<TransacaoResponseDTO> listarTodos(Pageable pageable) {
        return transacaoRepository.findAllByOrderByIdAsc(pageable)
                .map(TransacaoMapper::toDTO);
    }

    public TransacaoResponseDTO buscarDTOPorId(Long id) {
        var transacao = buscarEntidadePorId(id);

        return TransacaoMapper.toDTO(transacao);
    }

    @Transactional
    public TransacaoResponseDTO salvar(Transacao transacao) {
        Financeiro financeiro = financeiroService.buscarEntidadePorId(transacao.getFinanceiro().getId());

        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorTransacao = transacao.getValor();

        if (transacao.getTipo() == TipoTransacao.SAIDA) {
            if (saldoAtual.compareTo(valorTransacao) < 0) {
                throw new BadRequestException("Saldo insuficiente para realizar a transação de SAIDA!");
            }
            financeiro.setSaldoAtual(saldoAtual.subtract(valorTransacao));

        } else if (transacao.getTipo() == TipoTransacao.ENTRADA) {
            financeiro.setSaldoAtual(saldoAtual.add(valorTransacao));
        }

        financeiroService.atualizar(financeiro.getId(), financeiro);

        transacao.setFinanceiro(financeiro);
        var transacaoSalva = transacaoRepository.save(transacao);

        return TransacaoMapper.toDTO(transacaoSalva);
    }

    @Transactional
    public TransacaoResponseDTO atualizar(Long id, Transacao novaTransacao) {
        Transacao transacaoExistente = buscarEntidadePorId(id);

        Financeiro financeiro = transacaoExistente.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();

        BigDecimal valorTransacaoAntigo = transacaoExistente.getValor();
        TipoTransacao tipoTransacaoAntigo = transacaoExistente.getTipo();

        BigDecimal valorTransacaoNovo = novaTransacao.getValor();
        TipoTransacao tipoTransacaoNovo = novaTransacao.getTipo();

        // Desfaz o efeito antigo
        if (tipoTransacaoAntigo == TipoTransacao.SAIDA) {
            saldoAtual = saldoAtual.add(valorTransacaoAntigo);

        } else if (tipoTransacaoAntigo == TipoTransacao.ENTRADA) {
            saldoAtual = saldoAtual.subtract(valorTransacaoAntigo);
        }

        // Aplica o efeito novo
        if (tipoTransacaoNovo == TipoTransacao.SAIDA) {
            if (saldoAtual.compareTo(valorTransacaoNovo) < 0) {
                throw new BadRequestException("Saldo insuficiente para realizar a atualização de SAIDA!");
            }
            saldoAtual = saldoAtual.subtract(valorTransacaoNovo);

        } else if (tipoTransacaoNovo == TipoTransacao.ENTRADA) {
            saldoAtual = saldoAtual.add(valorTransacaoNovo);
        }

        financeiro.setSaldoAtual(saldoAtual);
        financeiroService.atualizar(financeiro.getId(), financeiro);

        transacaoExistente.setDescricao(novaTransacao.getDescricao());
        transacaoExistente.setValor(valorTransacaoNovo);
        transacaoExistente.setTipo(tipoTransacaoNovo);
        transacaoExistente.setCategoria(novaTransacao.getCategoria());

        var transacaoAtualizada = transacaoRepository.save(transacaoExistente);

        return TransacaoMapper.toDTO(transacaoAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        Transacao transacao = buscarEntidadePorId(id);

        Financeiro financeiro = transacao.getFinanceiro();
        BigDecimal saldoAtual = financeiro.getSaldoAtual();
        BigDecimal valorTransacao = transacao.getValor();

        switch (transacao.getTipo()) {
            case SAIDA -> saldoAtual = saldoAtual.add(valorTransacao);

            case ENTRADA -> {
                if (saldoAtual.compareTo(valorTransacao) < 0) {
                    throw new BadRequestException("Saldo insuficiente para remover esta ENTRADA. Isso deixaria o saldo negativo!");
                }
                saldoAtual = saldoAtual.subtract(valorTransacao);
            }

            case APLICACAO -> {
                // devolve valor ao financeiro e remove da meta
                saldoAtual = saldoAtual.add(valorTransacao);

                MetaFinanceira meta = metaFinanceiraRepository.findByFinanceiroAndNome(financeiro, transacao.getDescricao().replace("Aplicação na meta: ", ""))
                        .orElseThrow(() -> new EntityNotFoundException("Meta relacionada ao investimento não encontrada."));

                BigDecimal saldoMeta = meta.getValorAtual();

                if (saldoMeta.compareTo(valorTransacao) < 0) {
                    throw new BadRequestException("A meta não possui saldo suficiente para desfazer o investimento.");
                }

                meta.setValorAtual(saldoMeta.subtract(valorTransacao));
                metaFinanceiraRepository.save(meta);
            }

            case RESGATE -> {
                // retira valor do financeiro e adiciona à meta
                if (saldoAtual.compareTo(valorTransacao) < 0) {
                    throw new BadRequestException("Saldo insuficiente para remover este RESGATE.");
                }
                saldoAtual = saldoAtual.subtract(valorTransacao);

                MetaFinanceira meta = metaFinanceiraRepository.findByFinanceiroAndNome(financeiro, transacao.getDescricao().replace("Retirada da meta: ", ""))
                        .orElseThrow(() -> new EntityNotFoundException("Meta relacionada ao resgate não encontrada."));

                meta.setValorAtual(meta.getValorAtual().add(valorTransacao));
                metaFinanceiraRepository.save(meta);
            }
        }

        financeiro.setSaldoAtual(saldoAtual);
        financeiroService.atualizar(financeiro.getId(), financeiro);

        transacaoRepository.deleteById(id);
    }

    public Transacao buscarEntidadePorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação com id: " + id + " não encontrada"));
    }

    public List<Transacao> buscarTransacoesPorFinanceiro(Financeiro financeiro) {
        return transacaoRepository.findByFinanceiro(financeiro);
    }

    public void atualizarVariasTransacoes(List<Transacao> transacoes) {
        transacaoRepository.saveAll(transacoes);
    }

    public void deletarVariasTransacoes(List<Transacao> transacoes) {
        transacaoRepository.deleteAll(transacoes);
    }
}
