package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifeboard.model.enums.CategoriaTransacao;
import com.lifeboard.model.enums.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;


public class TransacaoRequestDTO {

    @NotNull(message = "Descrição é obrigatório.")
    @Size(min = 3, max = 150, message = "A descrição da transação deve ter entre 3 e 150 caracteres")
    private String descricao;

    @NotNull(message = "Valor é obrigatório.")
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor da transação deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "Tipo é obrigatório.")
    private TipoTransacao tipo;

    @NotNull(message = "Categoria é obrigatório.")
    private CategoriaTransacao categoria;

    @NotNull(message = "O ID de um Financeiro é obrigatório.")
    @JsonProperty("id_financeiro")
    private Long idFinanceiro;

    public TransacaoRequestDTO() {
    }

    public TransacaoRequestDTO(String descricao, BigDecimal valor, TipoTransacao tipo, CategoriaTransacao categoria, Long idFinanceiro) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.idFinanceiro = idFinanceiro;
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
