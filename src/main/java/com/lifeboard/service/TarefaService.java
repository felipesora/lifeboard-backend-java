package com.lifeboard.service;

import com.lifeboard.dto.TarefaResponseDTO;
import com.lifeboard.mapper.TarefaMapper;
import com.lifeboard.model.Tarefa;
import com.lifeboard.model.Transacao;
import com.lifeboard.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Page<TarefaResponseDTO> listarTodos(Pageable pageable) {
        return tarefaRepository.findAllByOrderByIdAsc(pageable)
                .map(TarefaMapper::toDTO);
    }

    public TarefaResponseDTO buscarDTOPorId(Long id) {
        var tarefa = buscarEntidadePorId(id);

        return TarefaMapper.toDTO(tarefa);
    }

    public TarefaResponseDTO salvar(Tarefa tarefa) {
        var tarefaSalva = tarefaRepository.save(tarefa);

        return TarefaMapper.toDTO(tarefaSalva);
    }

    public TarefaResponseDTO atualizar(Long id, Tarefa novaTarefa) {
        Tarefa tarefaExistente = buscarEntidadePorId(id);

        tarefaExistente.setTitulo(novaTarefa.getTitulo());
        tarefaExistente.setDescricao(novaTarefa.getDescricao());
        tarefaExistente.setPrioridade(novaTarefa.getPrioridade());
        tarefaExistente.setStatus(novaTarefa.getStatus());
        tarefaExistente.setDataLimite(novaTarefa.getDataLimite());

        var tarefaAtualizada = tarefaRepository.save(tarefaExistente);

        return TarefaMapper.toDTO(tarefaAtualizada);
    }

    public void deletar(Long id) {
        var tarefa = buscarEntidadePorId(id);
        tarefaRepository.delete(tarefa);
    }

    public Tarefa buscarEntidadePorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + id + " não encontrada"));
    }
}
