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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var usuario = UsuarioMapper.toDTO(service.buscarPorId(id));
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid UsuarioRequestDTO dto, UriComponentsBuilder uriBuilder) {
        var usuario = UsuarioMapper.toDTO(service.salvar(UsuarioMapper.toEntity(dto)));

        var uri = uriBuilder.path("/api/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto){
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setId(id);

        Usuario atualizado = service.atualizar(id, usuario);

        UsuarioResponseDTO responseDTO = UsuarioMapper.toDTO(atualizado);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/foto")
    public ResponseEntity<Void> atualizarFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        service.atualizarFotoPerfil(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/foto", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id) {
        byte[] imagem = service.buscarFotoPerfil(id);
        return ResponseEntity.ok(imagem);
    }

    @DeleteMapping("/{id}/foto")
    public ResponseEntity<Void> removerFoto(@PathVariable Long id) {
        service.removerFotoPerfil(id);
        return ResponseEntity.ok().build();
    }
}
