package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.MetaFinanceira;
import com.lifeboard.model.Transacao;
import java.math.BigDecimal;
import java.util.List;

@JsonPropertyOrder({ "id_financeiro", "saldo_atual", "salario_mensal", "id_usuario", "transacoes", "metas"})
public class FinanceiroResponseDTO {

    @JsonProperty("id_financeiro")
    private Long id;
    @JsonProperty("saldo_atual")
    private BigDecimal saldoAtual;
    @JsonProperty("salario_mensal")
    private BigDecimal salarioMensal;
    @JsonProperty("id_usuario")
    private Long usuarioId;
    private List<TransacaoResponseDTO> transacoes;
    private List<MetaFinanceiraResponseDTO> metas;

    public FinanceiroResponseDTO() {
    }

    public FinanceiroResponseDTO(Long id, BigDecimal saldoAtual, BigDecimal salarioMensal, Long usuarioId, List<TransacaoResponseDTO> transacoes, List<MetaFinanceiraResponseDTO> metas) {
        this.id = id;
        this.saldoAtual = saldoAtual;
        this.salarioMensal = salarioMensal;
        this.usuarioId = usuarioId;
        this.transacoes = transacoes;
        this.metas = metas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public BigDecimal getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(BigDecimal salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<TransacaoResponseDTO> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoResponseDTO> transacoes) {
        this.transacoes = transacoes;
    }

    public List<MetaFinanceiraResponseDTO> getMetas() {
        return metas;
    }

    public void setMetas(List<MetaFinanceiraResponseDTO> metas) {
        this.metas = metas;
    }
}
