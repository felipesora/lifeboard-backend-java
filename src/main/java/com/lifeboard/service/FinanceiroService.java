package com.lifeboard.service;

import com.lifeboard.model.Financeiro;
import com.lifeboard.repository.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceiroService {

    @Autowired
    private FinanceiroRepository repository;

    public Page<Financeiro> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Financeiro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financeiro não encontrado com id: " + id));
    }

    public Financeiro salvar(Financeiro entity) {
        return repository.save(entity);
    }

    public Financeiro atualizar(Long id, Financeiro novoFinanceiro) {
        Financeiro financeiroExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financeiro não encontrado com id: " + id));

        financeiroExistente.setSaldoAtual(novoFinanceiro.getSaldoAtual());
        financeiroExistente.setSalarioMensal(novoFinanceiro.getSalarioMensal());
        financeiroExistente.setUsuario(novoFinanceiro.getUsuario());
        financeiroExistente.setTransacoes(novoFinanceiro.getTransacoes());
        financeiroExistente.setMetas(novoFinanceiro.getMetas());

        return repository.save(financeiroExistente);
    }

    public String deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Financeiro deletado com sucesso!";
        }
        return "Erro ao deletar! Financeiro com " + id + " não encontrado.";
    }
}
