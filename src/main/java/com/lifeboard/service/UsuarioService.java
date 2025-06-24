package com.lifeboard.service;

import com.lifeboard.exception.ResourceNotFoundException;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, Long, UsuarioRepository> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Usuario atualizar(Long id, Usuario novoUsuario) {
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setEmail(novoUsuario.getEmail());
        usuarioExistente.setSenha(novoUsuario.getSenha());
        usuarioExistente.setFinanceiro(novoUsuario.getFinanceiro());

        return repository.save(usuarioExistente);
    }
}
