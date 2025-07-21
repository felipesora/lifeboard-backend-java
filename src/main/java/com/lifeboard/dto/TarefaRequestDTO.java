package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifeboard.model.enums.Prioridade;
import com.lifeboard.model.enums.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TarefaRequestDTO {

    @NotBlank(message = "Titulo é obrigatória.")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "Prioridade é obrigatória.")
    private Prioridade prioridade;

    @NotNull(message = "Status é obrigatório.")
    private StatusTarefa status;

    @NotNull(message = "Data Limite é obrigatória.")
    @JsonProperty("data_limite")
    private LocalDate dataLimite;

    @NotNull(message = "O ID do Usuário é obrigatório.")
    @JsonProperty("id_usuario")
    private Long usuarioId;

    public TarefaRequestDTO() {
    }

    public TarefaRequestDTO(String titulo, String descricao, Prioridade prioridade, StatusTarefa status, LocalDate dataLimite, Long usuarioId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
