package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifeboard.model.enums.StatusMeta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MetaFinanceiraRequestDTO {

    @NotNull(message = "Nome da Meta é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome da meta deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "O valor da meta é obrigatório.")
    private BigDecimal valorMeta;

    @NotNull(message = "A data limite da meta é obrigatório.")
    private LocalDate dataLimite;

    @NotNull(message = "O ID de um Financeiro é obrigatório.")
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public MetaFinanceiraRequestDTO() {
    }

    public MetaFinanceiraRequestDTO(String nome, BigDecimal valorMeta, LocalDate dataLimite, Long idFinanceiro) {
        this.nome = nome;
        this.valorMeta = valorMeta;
        this.dataLimite = dataLimite;
        this.idFinanceiro = idFinanceiro;
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

    public Long getIdFinanceiro() {
        return idFinanceiro;
    }

    public void setIdFinanceiro(Long idFinanceiro) {
        this.idFinanceiro = idFinanceiro;
    }
}
