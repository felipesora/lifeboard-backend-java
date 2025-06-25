package com.lifeboard.controller;

import com.lifeboard.dto.MetaFinanceiraRequestDTO;
import com.lifeboard.dto.MetaFinanceiraResponseDTO;
import com.lifeboard.mapper.MetaFinanceiraMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.MetaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/metas")
public class MetaFinanceiraController {

    private final MetaFinanceiraService metaFinanceiraService;
    private final FinanceiroService financeiroService;

    public MetaFinanceiraController(MetaFinanceiraService metaFinanceiraService, FinanceiroService financeiroService) {
        this.metaFinanceiraService = metaFinanceiraService;
        this.financeiroService = financeiroService;
    }

    @GetMapping
    public List<MetaFinanceiraResponseDTO> listarTodos() {
        return metaFinanceiraService.listarTodos()
                .stream()
                .map(MetaFinanceiraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MetaFinanceiraResponseDTO buscarPorId(@PathVariable Long id) {
        return MetaFinanceiraMapper.toDTO(metaFinanceiraService.buscarPorId(id));
    }

    @PostMapping
    public MetaFinanceiraResponseDTO salvar(@RequestBody @Valid MetaFinanceiraRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());

        MetaFinanceira meta = MetaFinanceiraMapper.toEntity(dto, financeiro);

        return MetaFinanceiraMapper.toDTO(metaFinanceiraService.salvar(meta));
    }

    @PutMapping("/{id}")
    public MetaFinanceiraResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid MetaFinanceiraRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());

        MetaFinanceira meta = MetaFinanceiraMapper.toEntity(dto, financeiro);
        meta.setId(id);

        return MetaFinanceiraMapper.toDTO(metaFinanceiraService.atualizar(id,meta));
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        return metaFinanceiraService.deletar(id);
    }
}
