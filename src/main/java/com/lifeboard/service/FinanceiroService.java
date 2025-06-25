package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    @Override
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
