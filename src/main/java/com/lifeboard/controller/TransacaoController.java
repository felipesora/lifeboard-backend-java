package com.lifeboard.controller;

import com.lifeboard.dto.*;
import com.lifeboard.mapper.TransacaoMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Transacao;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private FinanceiroService financeiroService;


    @GetMapping
    public ResponseEntity<Page<TransacaoResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        Page<Transacao> transacoes = transacaoService.listarTodos(paginacao);
        Page<TransacaoResponseDTO> dtoPage = transacoes.map(TransacaoMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var transacao = TransacaoMapper.toDTO(transacaoService.buscarPorId(id));
        return ResponseEntity.ok(transacao);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid TransacaoRequestDTO dto, UriComponentsBuilder uriBuilder){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        Transacao transacao = TransacaoMapper.toEntity(dto, financeiro);
        var transacaoSalva = TransacaoMapper.toDTO(transacaoService.salvar(transacao));

        var uri = uriBuilder.path("/api/transacoes/{id}").buildAndExpand(transacaoSalva.getId()).toUri();

        return ResponseEntity.created(uri).body(transacaoSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid TransacaoRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        Transacao transacao = TransacaoMapper.toEntity(dto, financeiro);
        transacao.setId(id);

        Transacao atualizado = transacaoService.atualizar(id,transacao);
        TransacaoResponseDTO responseDTO = TransacaoMapper.toDTO(atualizado);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        transacaoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
