package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.interfaces.IBaseService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements IBaseService<T, ID> {

    protected final R repository;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }


    @Override
    public List<T> listarTodos() {
        return repository.findAll();
    }

    @Override
    public T buscarPorId(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objeto com ID " + id + " não encontrado."));
    }

    @Override
    public T salvar(T entity) {
        return repository.save(entity);
    }

    @Override
    public T atualizar(ID id, T entity) {
        if (repository.existsById(id)){
            return repository.save(entity);
        }
        throw new ResourceNotFoundException("Não é possível atualizar. Objeto com ID " + id + " não encontrado.");
    }

    @Override
    public String deletar(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Objeto deletado com sucesso!";
        }
        throw new ResourceNotFoundException("Não é possível deletar. Objeto com ID " + id + " não encontrado.");
    }
}
