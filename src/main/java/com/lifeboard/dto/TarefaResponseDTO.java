package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.enums.Prioridade;
import com.lifeboard.model.enums.StatusTarefa;
import java.time.LocalDate;

@JsonPropertyOrder({ "id_tarefa", "titulo", "descricao", "prioridade", "status", "data_limite", "id_usuario"})
public class TarefaResponseDTO {

    @JsonProperty("id_tarefa")
    private Long id;

    private String titulo;

    private String descricao;

    private Prioridade prioridade;

    private StatusTarefa status;

    @JsonProperty("data_limite")
    private LocalDate dataLimite;

    @JsonProperty("id_usuario")
    private Long usuarioId;

    public TarefaResponseDTO() {
    }

    public TarefaResponseDTO(Long id, String titulo, String descricao, Prioridade prioridade, StatusTarefa status, LocalDate dataLimite, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
