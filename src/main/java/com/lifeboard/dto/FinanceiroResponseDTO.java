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
    private List<Transacao> transacoes;
    private List<MetaFinanceira> metas;

    public FinanceiroResponseDTO() {
    }

    public FinanceiroResponseDTO(Long id, BigDecimal saldoAtual, BigDecimal salarioMensal, Long usuarioId, List<Transacao> transacoes, List<MetaFinanceira> metas) {
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

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    public List<MetaFinanceira> getMetas() {
        return metas;
    }

    public void setMetas(List<MetaFinanceira> metas) {
        this.metas = metas;
    }
}
