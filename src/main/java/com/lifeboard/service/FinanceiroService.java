package com.lifeboard.service;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public String deletar(Long id) {
        Financeiro financeiro = buscarPorId(id);

        // Quebra o vínculo com o usuário
        Usuario usuario = financeiro.getUsuario();
        if (usuario != null) {
            usuario.setFinanceiro(null); // remove o vínculo
        }

        // Remove filhos para garantir o orphanRemoval
        if (financeiro.getTransacoes() != null) {
            financeiro.getTransacoes().clear();
        }

        if (financeiro.getMetas() != null) {
            financeiro.getMetas().clear();
        }

        repository.save(financeiro); // atualiza estado

        repository.delete(financeiro); // agora pode deletar

        return "Financeiro deletado com sucesso!";
    }
}
