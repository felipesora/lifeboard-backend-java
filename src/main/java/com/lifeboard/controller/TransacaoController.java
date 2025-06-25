package com.lifeboard.controller;

import com.lifeboard.dto.*;
import com.lifeboard.mapper.TransacaoMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Transacao;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final FinanceiroService financeiroService;

    public TransacaoController(TransacaoService transacaoService, FinanceiroService financeiroService) {
        this.transacaoService = transacaoService;
        this.financeiroService = financeiroService;
    }

    @GetMapping
    public List<TransacaoResponseDTO> listarTodos() {
        return transacaoService.listarTodos()
                .stream()
                .map(TransacaoMapper::toDTO)
                .collect(Collectors.toList());
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
