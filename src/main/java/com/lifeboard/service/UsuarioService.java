package com.lifeboard.service;

import com.lifeboard.dto.UsuarioResponseDTO;
import com.lifeboard.mapper.UsuarioMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.FinanceiroRepository;
import com.lifeboard.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAllByOrderByIdAsc(pageable)
                .map(UsuarioMapper::toDTO);
    }

    public UsuarioResponseDTO buscarDTOPorId(Long id) {
        var usuario = buscarEntidadePorId(id);

        return UsuarioMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO salvar(Usuario novoUsuario) {
        criptografarSenhaSeNecessario(novoUsuario);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        if (usuarioSalvo.getFinanceiro() == null) {
            Financeiro financeiro = new Financeiro();
            financeiro.setSaldoAtual(BigDecimal.ZERO);
            financeiro.setSalarioMensal(BigDecimal.ZERO);
            financeiro.setUsuario(usuarioSalvo);

            financeiroRepository.save(financeiro);
        }

        return UsuarioMapper.toDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, Usuario novoUsuario) {
        Usuario usuarioExistente = buscarEntidadePorId(id);

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setEmail(novoUsuario.getEmail());

        criptografarSenhaSeNecessario(novoUsuario);
        if (novoUsuario.getSenha() != null && !novoUsuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(novoUsuario.getSenha());
        }

        // Evita sobrescrever o financeiro se vier nulo
        if (novoUsuario.getFinanceiro() != null) {
            usuarioExistente.setFinanceiro(novoUsuario.getFinanceiro());
        }

        var usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return UsuarioMapper.toDTO(usuarioAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        var usuario = buscarEntidadePorId(id);
        usuarioRepository.delete(usuario);
    }

    public void atualizarFotoPerfil(Long id, MultipartFile file) throws IOException {
        Usuario usuario = buscarEntidadePorId(id);

        usuario.setFotoPerfil(file.getBytes());
        usuarioRepository.save(usuario);
    }

    public byte[] buscarFotoPerfil(Long id) {
        Usuario usuario = buscarEntidadePorId(id);

        return usuario.getFotoPerfil();
    }

    public void removerFotoPerfil(Long id) {
        Usuario usuario = buscarEntidadePorId(id);

        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);
    }

    public Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com id: " + id + " não encontrado"));
    }

    private void criptografarSenhaSeNecessario(Usuario usuario) {
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
    }
}
