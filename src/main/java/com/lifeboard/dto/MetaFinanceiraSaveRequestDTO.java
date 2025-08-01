package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MetaFinanceiraSaveRequestDTO {

    @NotNull(message = "Nome da Meta é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome da meta deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "O valor da meta é obrigatório.")
    @JsonProperty("valor_meta")
    private BigDecimal valorMeta;

    @NotNull(message = "O valor atual é obrigatório.")
    @JsonProperty("valor_atual")
    private BigDecimal valorAtual;

    @NotNull(message = "A data limite da meta é obrigatório.")
    @JsonProperty("data_limite")
    private LocalDate dataLimite;

    @NotNull(message = "O ID de um Financeiro é obrigatório.")
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public MetaFinanceiraSaveRequestDTO() {
    }

    public MetaFinanceiraSaveRequestDTO(String nome, BigDecimal valorMeta, BigDecimal valorAtual, LocalDate dataLimite, Long idFinanceiro) {
        this.nome = nome;
        this.valorMeta = valorMeta;
        this.valorAtual = valorAtual;
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

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
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
