package com.lifeboard.controller;

import com.lifeboard.dto.FinanceiroRequestDTO;
import com.lifeboard.dto.FinanceiroResponseDTO;
import com.lifeboard.mapper.FinanceiroMapper;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.Usuario;
import com.lifeboard.service.FinanceiroService;
import com.lifeboard.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/financeiros")
public class FinanceiroController {

    private final FinanceiroService financeiroService;
    private final UsuarioService usuarioService;

    public FinanceiroController(FinanceiroService financeiroService, UsuarioService usuarioService) {
        this.financeiroService = financeiroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<FinanceiroResponseDTO> listarTodos() {
        return financeiroService.listarTodos()
                .stream()
                .map(FinanceiroMapper::toDTO)
                .collect(Collectors.toList());
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
