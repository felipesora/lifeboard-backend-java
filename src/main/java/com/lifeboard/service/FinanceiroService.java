package com.lifeboard.service;

import com.lifeboard.dto.FinanceiroResponseDTO;
import com.lifeboard.mapper.FinanceiroMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Page<FinanceiroResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable)
                .map(FinanceiroMapper::toDTO);
    }

    public FinanceiroResponseDTO buscarDTOPorId(Long id) {
        var financeiro = buscarEntidadePorId(id);

        return FinanceiroMapper.toDTO(financeiro);
    }

    @Transactional
    public FinanceiroResponseDTO salvar(Financeiro novoFinanceiro) {
        var financeiro = repository.save(novoFinanceiro);

        return FinanceiroMapper.toDTO(financeiro);
    }

    @Transactional
    public FinanceiroResponseDTO atualizar(Long id, Financeiro novoFinanceiro) {
        var financeiroExistente = buscarEntidadePorId(id);

        financeiroExistente.setSaldoAtual(novoFinanceiro.getSaldoAtual());
        financeiroExistente.setSalarioMensal(novoFinanceiro.getSalarioMensal());
        financeiroExistente.setUsuario(novoFinanceiro.getUsuario());

        var financeiroAtualizado = repository.save(financeiroExistente);

        return FinanceiroMapper.toDTO(financeiroAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        var financeiro = buscarEntidadePorId(id);

        // Quebra o vínculo com o usuário
        Usuario usuario = financeiro.getUsuario();
        if (usuario != null) {
            usuario.setFinanceiro(null); // remove o vínculo
        }

        limparRelacionamentos(financeiro);

        repository.save(financeiro);
        repository.delete(financeiro);
    }

    public Financeiro buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Financeiro com id: " + id + " não encontrado"));
    }

    private void limparRelacionamentos(Financeiro financeiro) {
        if (financeiro.getTransacoes() != null) {
            financeiro.getTransacoes().clear();
        }
        if (financeiro.getMetas() != null) {
            financeiro.getMetas().clear();
        }
    }
}
