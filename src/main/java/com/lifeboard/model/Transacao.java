package com.lifeboard.model;

import com.lifeboard.model.enums.CategoriaTransacao;
import com.lifeboard.model.enums.TipoTransacao;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lb_transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lb_transacao_seq")
    @SequenceGenerator(name = "lb_transacao_seq", sequenceName = "LB_TRANSACAO_SEQ", allocationSize = 1)
    @Column(name = "id_transacao")
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoTransacao tipo; // ENTRADA ou SAIDA

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CategoriaTransacao categoria;

    @ManyToOne
    @JoinColumn(name = "id_financeiro", nullable = false)
    private Financeiro financeiro;

    public Transacao() {
    }

    public Transacao(Long id, String descricao, BigDecimal valor, TipoTransacao tipo, LocalDateTime data, CategoriaTransacao categoria, Financeiro financeiro) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
        this.categoria = categoria;
        this.financeiro = financeiro;
    }

    @PrePersist
    protected void onCreate() {
        this.data = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public CategoriaTransacao getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaTransacao categoria) {
        this.categoria = categoria;
    }

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        this.financeiro = financeiro;
    }
}
