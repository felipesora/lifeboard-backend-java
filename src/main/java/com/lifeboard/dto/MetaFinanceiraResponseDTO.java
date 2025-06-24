package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.enums.StatusMeta;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonPropertyOrder({ "id_meta", "valor_meta", "data_limite", "status", "id_financeiro"})

public class MetaFinanceiraResponseDTO {

    @JsonProperty("id_meta")
    private Long id;
    @JsonProperty("valor_meta")
    private BigDecimal valorMeta;
    @JsonProperty("data_limite")
    private LocalDate dataLimite;
    private StatusMeta status;
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public MetaFinanceiraResponseDTO() {
    }

    public MetaFinanceiraResponseDTO(Long id, BigDecimal valorMeta, LocalDate dataLimite, StatusMeta status, Long idFinanceiro) {
        this.id = id;
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
