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
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var meta = MetaFinanceiraMapper.toDTO(metaFinanceiraService.buscarPorId(id));
        return ResponseEntity.ok(meta);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid MetaFinanceiraSaveRequestDTO dto, UriComponentsBuilder uriBuilder){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        MetaFinanceira meta = MetaFinanceiraMapper.toEntitySave(dto, financeiro);

        var metaSalva = MetaFinanceiraMapper.toDTO(metaFinanceiraService.salvar(meta));

        var uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(metaSalva.getId()).toUri();

        return ResponseEntity.created(uri).body(metaSalva);
    }

    @PostMapping("/{id}/adicionar-saldo")
    public ResponseEntity<?> adicionarSaldo(@PathVariable Long id, @RequestBody @Valid SaldoRequest saldoRequest) {
        metaFinanceiraService.adicionarSaldo(id, saldoRequest.valor());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/retirar-saldo")
    public ResponseEntity<?> retirarSaldo(@PathVariable Long id, @RequestBody @Valid SaldoRequest saldoRequest) {
        metaFinanceiraService.retirarSaldo(id, saldoRequest.valor());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid MetaFinanceiraUpdateRequestDTO dto){
        Financeiro financeiro = financeiroService.buscarPorId(dto.getIdFinanceiro());
        MetaFinanceira meta = MetaFinanceiraMapper.toEntityUpdate(dto, financeiro);
        meta.setId(id);

        MetaFinanceira atualizado = metaFinanceiraService.atualizar(id,meta);
        MetaFinanceiraResponseDTO responseDTO = MetaFinanceiraMapper.toDTO(atualizado);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        metaFinanceiraService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
