package com.lifeboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "lb_financeiros")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lb_financeiro_seq")
    @SequenceGenerator(name = "lb_financeiro_seq", sequenceName = "LB_financeiro_SEQ", allocationSize = 1)
    @Column(name = "id_financeiro")
    @JsonProperty("id_financeiro")
    private Long id;

    @Column(name = "saldo_atual", nullable = false)
    private BigDecimal saldoAtual;

    @Column(name = "salario", nullable = false)
    private BigDecimal salarioMensal;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetaFinanceira> metas;

    public Financeiro() {
    }

    public Financeiro(Long id, BigDecimal saldoAtual, BigDecimal salarioMensal, Usuario usuario, List<Transacao> transacoes, List<MetaFinanceira> metas) {
        this.id = id;
        this.saldoAtual = saldoAtual;
        this.salarioMensal = salarioMensal;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
