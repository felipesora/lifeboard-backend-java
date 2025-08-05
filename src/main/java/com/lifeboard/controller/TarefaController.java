package com.lifeboard.controller;

import com.lifeboard.dto.FinanceiroRequestDTO;
import com.lifeboard.dto.TarefaRequestDTO;
import com.lifeboard.dto.TarefaResponseDTO;
import com.lifeboard.mapper.FinanceiroMapper;
import com.lifeboard.mapper.TarefaMapper;
import com.lifeboard.mapper.UsuarioMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Tarefa;
import com.lifeboard.model.Usuario;
import com.lifeboard.service.TarefaService;
import com.lifeboard.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/tarefas")
@SecurityRequirement(name = "bearer-key")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma página de tarefas com paginação e ordenação")
    @Parameters({
            @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página", example = "10"),
            @Parameter(name = "sort", description = "Campo para ordenação. Ex: nome,asc ou id,desc", example = "nome,asc")
    })
    @GetMapping
    public ResponseEntity<Page<TarefaResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"})Pageable paginacao) {
        Page<Tarefa> tarefas = tarefaService.listarTodos(paginacao);
        Page<TarefaResponseDTO> dtoPage = tarefas.map(TarefaMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Buscar tarefa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var tarefa = TarefaMapper.toDTO(tarefaService.buscarPorId(id));
        return ResponseEntity.ok(tarefa);
    }

    @Operation(summary = "Cadastrar uma nova tarefa")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid TarefaRequestDTO dto, UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());
        Tarefa tarefa = TarefaMapper.toEntity(dto, usuario);

        var tarefaSalva = TarefaMapper.toDTO(tarefaService.salvar(tarefa));

        var uri = uriBuilder.path("/api/tarefas/{id}").buildAndExpand(tarefaSalva.getId()).toUri();

        return ResponseEntity.created(uri).body(tarefaSalva);
    }

    @Operation(summary = "Atualizar uma tarefa existente")
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid TarefaRequestDTO dto){
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());

        Tarefa existente = tarefaService.buscarPorId(id);

        existente.setTitulo(dto.getTitulo());
        existente.setDescricao(dto.getDescricao());
        existente.setStatus(dto.getStatus());
        existente.setPrioridade(dto.getPrioridade());
        existente.setDataLimite(dto.getDataLimite());
        existente.setUsuario(usuario);

        Tarefa atualizado = tarefaService.atualizar(id, existente);

        return ResponseEntity.ok(TarefaMapper.toDTO(atualizado));
    }

    @Operation(summary = "Deletar uma tarefa")
    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        tarefaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
