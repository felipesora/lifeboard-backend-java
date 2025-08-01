package com.lifeboard.service;

import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.FinanceiroRepository;
import com.lifeboard.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAllByOrderByIdAsc(pageable);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }

    public Usuario salvar(Usuario entity) {
        String senhaCriptografada = passwordEncoder.encode(entity.getSenha());
        entity.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(entity);

        if (usuarioSalvo.getFinanceiro() == null) {
            Financeiro financeiro = new Financeiro();
            financeiro.setSaldoAtual(BigDecimal.ZERO);
            financeiro.setSalarioMensal(BigDecimal.ZERO);
            financeiro.setUsuario(usuarioSalvo);

            financeiroRepository.save(financeiro);
        }

        return usuarioSalvo;
    }

    public Usuario atualizar(Long id, Usuario novoUsuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setEmail(novoUsuario.getEmail());

        // Se a senha veio preenchida, criptografa antes de atualizar
        if (novoUsuario.getSenha() != null && !novoUsuario.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
            usuarioExistente.setSenha(senhaCriptografada);
        }

        // Evita sobrescrever o financeiro se vier nulo
        if (novoUsuario.getFinanceiro() != null) {
            usuarioExistente.setFinanceiro(novoUsuario.getFinanceiro());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public String deletar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return "Usuário deletado com sucesso!";
        }
        throw new RuntimeException("Usuário com id " + id + " não encontrado.");
    }

    public void atualizarFotoPerfil(Long id, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        usuario.setFotoPerfil(file.getBytes());
        usuarioRepository.save(usuario);
    }

    public byte[] buscarFotoPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        return usuario.getFotoPerfil();
    }

    public void removerFotoPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);
    }
}
