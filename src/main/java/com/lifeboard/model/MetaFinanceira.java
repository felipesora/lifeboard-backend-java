package com.lifeboard.model;

import com.lifeboard.model.enums.StatusMeta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lb_metas_financeiras")
public class MetaFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lb_meta_seq")
    @SequenceGenerator(name = "lb_meta_seq", sequenceName = "LB_META_SEQ", allocationSize = 1)
    @Column(name = "id_meta")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valorMeta;

    @Column(nullable = false)
    private LocalDate dataLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMeta status; // EM_ANDAMENTO, CONCLUIDA, CANCELADA

    @ManyToOne
    @JoinColumn(name = "id_financeiro", nullable = false)
    private Financeiro financeiro;

    public MetaFinanceira() {
    }

    public MetaFinanceira(Long id, String nome, BigDecimal valorMeta, LocalDate dataLimite, StatusMeta status, Financeiro financeiro) {
        this.id = id;
        this.nome = nome;
        this.valorMeta = valorMeta;
        this.dataLimite = dataLimite;
        this.status = status;
        this.financeiro = financeiro;
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

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        this.financeiro = financeiro;
    }
}
