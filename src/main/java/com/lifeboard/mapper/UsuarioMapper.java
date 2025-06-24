package com.lifeboard.mapper;

import com.lifeboard.dto.UsuarioRequestDTO;
import com.lifeboard.dto.UsuarioResponseDTO;
import com.lifeboard.model.Usuario;

public class UsuarioMapper {

    public static UsuarioResponseDTO toDTO (Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getFinanceiro()
        );
    }

    public static Usuario toEntity(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuario;
    }
}
