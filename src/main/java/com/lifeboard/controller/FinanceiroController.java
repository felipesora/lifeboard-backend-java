package com.lifeboard.controller;

import com.lifeboard.dto.FinanceiroRequestDTO;
import com.lifeboard.dto.FinanceiroResponseDTO;
import com.lifeboard.mapper.FinanceiroMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/financeiros")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<Page<FinanceiroResponseDTO>> listarTodos(@PageableDefault(size = 10, page = 0, sort = {"id"}) Pageable paginacao) {
        Page<Financeiro> financeiros = financeiroService.listarTodos(paginacao);
        Page<FinanceiroResponseDTO> dtoPage = financeiros.map(FinanceiroMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public FinanceiroResponseDTO buscarPorId(@PathVariable Long id) {
        return FinanceiroMapper.toDTO(financeiroService.buscarPorId(id));
    }

    @PostMapping
    public FinanceiroResponseDTO salvar(@RequestBody @Valid FinanceiroRequestDTO dto){
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());

        Financeiro financeiro = FinanceiroMapper.toEntity(dto, usuario);

        return FinanceiroMapper.toDTO(financeiroService.salvar(financeiro));
    }

    @PutMapping("/{id}")
    public FinanceiroResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid FinanceiroRequestDTO dto){
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());

        Financeiro financeiro = FinanceiroMapper.toEntity(dto, usuario);
        financeiro.setId(id);

        return FinanceiroMapper.toDTO(financeiroService.atualizar(id,financeiro));
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        return financeiroService.deletar(id);
    }
}
