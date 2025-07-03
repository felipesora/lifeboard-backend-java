package com.lifeboard.controller;

import com.lifeboard.dto.MetaFinanceiraRequestDTO;
import com.lifeboard.dto.MetaFinanceiraResponseDTO;
import com.lifeboard.mapper.MetaFinanceiraMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.MetaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/metas")
public class MetaFinanceiraController {

    @Autowired
    private MetaFinanceiraService metaFinanceiraService;

    @Autowired
    private FinanceiroService financeiroService;


    @GetMapping
    public ResponseEntity<Page<MetaFinanceiraResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        Page<MetaFinanceira> metas = metaFinanceiraService.listarTodos(paginacao);
        Page<MetaFinanceiraResponseDTO> dtoPage = metas.map(MetaFinanceiraMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
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
