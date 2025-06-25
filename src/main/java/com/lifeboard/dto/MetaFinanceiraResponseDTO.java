package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.enums.StatusMeta;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonPropertyOrder({ "id_meta", "nome", "valor_meta", "data_limite", "status", "id_financeiro"})

public class MetaFinanceiraResponseDTO {

    @JsonProperty("id_meta")
    private Long id;
    private String nome;
    @JsonProperty("valor_meta")
    private BigDecimal valorMeta;
    @JsonProperty("data_limite")
    private LocalDate dataLimite;
    private StatusMeta status;
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public MetaFinanceiraResponseDTO() {
    }

    public MetaFinanceiraResponseDTO(Long id, String nome, BigDecimal valorMeta, LocalDate dataLimite, StatusMeta status, Long idFinanceiro) {
        this.id = id;
        this.nome = nome;
        this.valorMeta = valorMeta;
        this.dataLimite = dataLimite;
        this.status = status;
        this.idFinanceiro = idFinanceiro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(BigDecimal valorMeta) {
        this.valorMeta = valorMeta;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public StatusMeta getStatus() {
        return status;
    }

    public void setStatus(StatusMeta status) {
        this.status = status;
    }

    public Long getIdFinanceiro() {
        return idFinanceiro;
    }

    public void setIdFinanceiro(Long idFinanceiro) {
        this.idFinanceiro = idFinanceiro;
    }
}
