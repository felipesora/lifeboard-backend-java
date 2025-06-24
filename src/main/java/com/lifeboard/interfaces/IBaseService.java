package com.lifeboard.interfaces;

import java.util.List;

public interface IBaseService<T, ID> {
    List<T> listarTodos();
    T buscarPorId(ID id);
    T salvar(T entity);
    T atualizar(ID id, T entity);
    String deletar(ID id);
}
