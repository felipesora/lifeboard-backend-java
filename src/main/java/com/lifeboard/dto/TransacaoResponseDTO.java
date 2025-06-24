package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.enums.CategoriaTransacao;
import com.lifeboard.model.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_transacao", "descricao", "valor", "tipo", "data", "categoria", "id_financeiro"})

public class TransacaoResponseDTO {

    @JsonProperty("id_transacao")
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private LocalDateTime data;
    private CategoriaTransacao categoria;
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public TransacaoResponseDTO() {
    }

    public TransacaoResponseDTO(Long id, String descricao, BigDecimal valor, TipoTransacao tipo, LocalDateTime data, CategoriaTransacao categoria, Long idFinanceiro) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
        this.categoria = categoria;
        this.idFinanceiro = idFinanceiro;
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

    public Long getIdFinanceiro() {
        return idFinanceiro;
    }

    public void setIdFinanceiro(Long idFinanceiro) {
        this.idFinanceiro = idFinanceiro;
    }
}
