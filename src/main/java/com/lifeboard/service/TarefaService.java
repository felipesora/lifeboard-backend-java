package com.lifeboard.service;

import com.lifeboard.model.Tarefa;
import com.lifeboard.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    public Page<Tarefa> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com id: " + id));
    }

    public Tarefa salvar(Tarefa entity) {
        return repository.save(entity);
    }

    public Tarefa atualizar(Long id, Tarefa novaTarefa) {
        Tarefa tarefaExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com id: " + id));

        tarefaExistente.setTitulo(novaTarefa.getTitulo());
        tarefaExistente.setDescricao(novaTarefa.getDescricao());
        tarefaExistente.setPrioridade(novaTarefa.getPrioridade());
        tarefaExistente.setStatus(novaTarefa.getStatus());
        tarefaExistente.setDataLimite(novaTarefa.getDataLimite());

        return repository.save(tarefaExistente);
    }

    public String deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Tarefa deletada com sucesso!";
        }
        throw new EntityNotFoundException("Erro ao deletar! Tarefa com " + id + " não encontrada.");
    }
}
