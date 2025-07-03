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
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var financeiro = FinanceiroMapper.toDTO(financeiroService.buscarPorId(id));
        return ResponseEntity.ok(financeiro);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody @Valid FinanceiroRequestDTO dto, UriComponentsBuilder uriBuilder){
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());
        Financeiro financeiro = FinanceiroMapper.toEntity(dto, usuario);
        var financeiroSalvo = FinanceiroMapper.toDTO(financeiroService.salvar(financeiro));

        var uri = uriBuilder.path("/api/financeiros/{id}").buildAndExpand(financeiroSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(financeiroSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid FinanceiroRequestDTO dto){
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());

        Financeiro existente = financeiroService.buscarPorId(id);

        existente.setSaldoAtual(dto.getSaldoAtual());
        existente.setSalarioMensal(dto.getSalarioMensal());
        existente.setUsuario(usuario);

        Financeiro atualizado = financeiroService.atualizar(id, existente);

        return ResponseEntity.ok(FinanceiroMapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        financeiroService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
