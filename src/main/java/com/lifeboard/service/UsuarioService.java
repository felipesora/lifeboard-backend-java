package com.lifeboard.service;

import com.lifeboard.model.Usuario;
import com.lifeboard.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> listarTodos(Pageable pageable) {
        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }

    public Usuario salvar(Usuario entity) {
        String senhaCriptografada = passwordEncoder.encode(entity.getSenha());
        entity.setSenha(senhaCriptografada);

        return repository.save(entity);
    }

    public Usuario atualizar(Long id, Usuario novoUsuario) {
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setEmail(novoUsuario.getEmail());
        usuarioExistente.setSenha(novoUsuario.getSenha());
        usuarioExistente.setFinanceiro(novoUsuario.getFinanceiro());

        return repository.save(usuarioExistente);
    }

    public String deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Usuário deletado com sucesso!";
        }
        throw new RuntimeException("Usuário com id " + id + " não encontrado.");
    }
}
