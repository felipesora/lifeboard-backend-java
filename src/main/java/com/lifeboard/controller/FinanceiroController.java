package com.lifeboard.controller;

import com.lifeboard.dto.FinanceiroRequestDTO;
import com.lifeboard.dto.FinanceiroResponseDTO;
import com.lifeboard.mapper.FinanceiroMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.repository.UsuarioRepository;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/financeiros")
@SecurityRequirement(name = "bearer-key")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos os financeiros", description = "Retorna uma página de financeiros com paginação e ordenação")
    @Parameters({
            @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página", example = "10"),
            @Parameter(name = "sort", description = "Campo para ordenação. Ex: nome,asc ou id,desc", example = "nome,asc")
    })
    @GetMapping
    public ResponseEntity<Page<FinanceiroResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        var financeiros = financeiroService.listarTodos(paginacao);
        return ResponseEntity.ok(financeiros);
    }

    @Operation(summary = "Buscar financeiro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Financeiro encontrado"),
            @ApiResponse(responseCode = "404", description = "Financeiro não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDTO> buscarPorId(@PathVariable Long id) {
        var financeiro = financeiroService.buscarDTOPorId(id);
        return ResponseEntity.ok(financeiro);
    }

    @Operation(summary = "Cadastrar um novo financeiro")
    @ApiResponse(responseCode = "201", description = "Financeiro criado com sucesso")
    @PostMapping
    public ResponseEntity<FinanceiroResponseDTO> salvar(@RequestBody @Valid FinanceiroRequestDTO dto, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.buscarEntidadePorId(dto.getUsuarioId());

        Financeiro financeiro = FinanceiroMapper.toEntity(dto, usuario);
        var financeiroSalvo = financeiroService.salvar(financeiro);

        var uri = uriBuilder.path("/api/financeiros/{id}").buildAndExpand(financeiroSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(financeiroSalvo);
    }

    @Operation(summary = "Atualizar um financeiro existente")
    @PutMapping("/{id}")
    public ResponseEntity<FinanceiroResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FinanceiroRequestDTO dto){
        var usuario = usuarioService.buscarEntidadePorId(dto.getUsuarioId());

        Financeiro novoFinanceiro = FinanceiroMapper.toEntity(dto, usuario);

        var financeiroAtualizado = financeiroService.atualizar(id, novoFinanceiro);

        return ResponseEntity.ok(financeiroAtualizado);
    }

    @Operation(summary = "Deletar um financeiro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        financeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
