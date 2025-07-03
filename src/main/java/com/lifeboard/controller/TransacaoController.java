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
    public TransacaoResponseDTO buscarPorId(@PathVariable Long id) {
        return TransacaoMapper.toDTO(transacaoService.buscarPorId(id));
    }

    @PostMapping
    public TransacaoResponseDTO salvar(@RequestBody @Valid TransacaoRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());

        Transacao transacao = TransacaoMapper.toEntity(dto, financeiro);

        return TransacaoMapper.toDTO(transacaoService.salvar(transacao));
    }

    @PutMapping("/{id}")
    public TransacaoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid TransacaoRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());

        Transacao transacao = TransacaoMapper.toEntity(dto, financeiro);
        transacao.setId(id);

        return TransacaoMapper.toDTO(transacaoService.atualizar(id,transacao));
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        return transacaoService.deletar(id);
    }
}
