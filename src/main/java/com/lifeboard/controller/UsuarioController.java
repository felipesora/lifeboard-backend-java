package com.lifeboard.controller;

import com.lifeboard.dto.UsuarioRequestDTO;
import com.lifeboard.dto.UsuarioResponseDTO;
import com.lifeboard.mapper.UsuarioMapper;
import com.lifeboard.model.Usuario;
import com.lifeboard.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        Page<Usuario> usuarios = service.listarTodos(paginacao);
        Page<UsuarioResponseDTO> dtoPage = usuarios.map(UsuarioMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id) {
        return UsuarioMapper.toDTO(service.buscarPorId(id));
    }

    @PostMapping
    public UsuarioResponseDTO salvar(@RequestBody @Valid UsuarioRequestDTO dto){
        return UsuarioMapper.toDTO(service.salvar(UsuarioMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto){
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setId(id);
        return UsuarioMapper.toDTO(service.atualizar(id,usuario));
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        return service.deletar(id);
    }
}
