package com.lifeboard.controller;

import com.lifeboard.dto.MetaFinanceiraUpdateRequestDTO;
import com.lifeboard.dto.SaldoRequest;
import com.lifeboard.dto.MetaFinanceiraSaveRequestDTO;
import com.lifeboard.dto.MetaFinanceiraResponseDTO;
import com.lifeboard.mapper.MetaFinanceiraMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.MetaFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/metas")
@SecurityRequirement(name = "bearer-key")
public class MetaFinanceiraController {

    @Autowired
    private MetaFinanceiraService metaFinanceiraService;

    @Autowired
    private FinanceiroService financeiroService;


    @Operation(summary = "Listar todas as metas financeiras", description = "Retorna uma página de metas financeiras com paginação e ordenação")
    @Parameters({
            @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página", example = "10"),
            @Parameter(name = "sort", description = "Campo para ordenação. Ex: nome,asc ou id,desc", example = "nome,asc")
    })
    @GetMapping
    public ResponseEntity<Page<MetaFinanceiraResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        Page<MetaFinanceira> metas = metaFinanceiraService.listarTodos(paginacao);
        Page<MetaFinanceiraResponseDTO> dtoPage = metas.map(MetaFinanceiraMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Buscar meta financeira por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meta financeira encontrada"),
            @ApiResponse(responseCode = "404", description = "Meta financeira não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var meta = MetaFinanceiraMapper.toDTO(metaFinanceiraService.buscarPorId(id));
        return ResponseEntity.ok(meta);
    }

    @Operation(summary = "Cadastrar uma nova meta financeira")
    @ApiResponse(responseCode = "201", description = "Meta financeira criada com sucesso")
    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid MetaFinanceiraSaveRequestDTO dto, UriComponentsBuilder uriBuilder){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        MetaFinanceira meta = MetaFinanceiraMapper.toEntitySave(dto, financeiro);

        var metaSalva = MetaFinanceiraMapper.toDTO(metaFinanceiraService.salvar(meta));

        var uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(metaSalva.getId()).toUri();

        return ResponseEntity.created(uri).body(metaSalva);
    }

    @Operation(
            summary = "Adicionar saldo à meta financeira",
            description = "Adiciona um valor ao saldo atual de uma meta financeira específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta financeira não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping("/{id}/adicionar-saldo")
    public ResponseEntity<?> adicionarSaldo(@PathVariable Long id, @RequestBody @Valid SaldoRequest saldoRequest) {
        metaFinanceiraService.adicionarSaldo(id, saldoRequest.valor());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Retirar saldo da meta financeira",
            description = "Remove um valor do saldo atual de uma meta financeira específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo retirado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta financeira não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente", content = @Content)
    })
    @PostMapping("/{id}/retirar-saldo")
    public ResponseEntity<?> retirarSaldo(@PathVariable Long id, @RequestBody @Valid SaldoRequest saldoRequest) {
        metaFinanceiraService.retirarSaldo(id, saldoRequest.valor());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualizar uma meta financeira")
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid MetaFinanceiraUpdateRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        MetaFinanceira meta = MetaFinanceiraMapper.toEntityUpdate(dto, financeiro);
        meta.setId(id);

        MetaFinanceira atualizado = metaFinanceiraService.atualizar(id,meta);
        MetaFinanceiraResponseDTO responseDTO = MetaFinanceiraMapper.toDTO(atualizado);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Deletar uma meta financeira")
    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        metaFinanceiraService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
