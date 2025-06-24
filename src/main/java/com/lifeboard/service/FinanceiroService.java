package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.model.Financeiro;
import com.lifeboard.repository.FinanceiroRepository;
import java.util.List;

public class FinanceiroService extends BaseServiceImpl<Financeiro, Long, FinanceiroRepository> {

    public FinanceiroService(FinanceiroRepository repository) {
        super(repository);
    }

    @Override
    public List<Financeiro> listarTodos() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Financeiro atualizar(Long id, Financeiro novoFinanceiro) {
        Financeiro financeiroExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financeiro não encontrado"));

        financeiroExistente.setSaldoAtual(novoFinanceiro.getSaldoAtual());
        financeiroExistente.setSalarioMensal(novoFinanceiro.getSalarioMensal());
        financeiroExistente.setUsuario(novoFinanceiro.getUsuario());
        financeiroExistente.setTransacoes(novoFinanceiro.getTransacoes());
        financeiroExistente.setMetas(novoFinanceiro.getMetas());

        return repository.save(financeiroExistente);
    }
}
