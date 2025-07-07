package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class FinanceiroRequestDTO {

    @NotNull(message = "Saldo Atual é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser igual ou maior que zero.")
    @JsonProperty("saldo_atual")
    private BigDecimal saldoAtual;

    @NotNull(message = "Salário é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O salário deve ser maior que zero.")
    @JsonProperty("salario_mensal")
    private BigDecimal salarioMensal;

    @NotNull(message = "O ID de um Usuário é obrigatório.")
    @JsonProperty("id_usuario")
    private Long usuarioId;

    public FinanceiroRequestDTO() {
    }

    public FinanceiroRequestDTO(BigDecimal saldoAtual, BigDecimal salarioMensal, Long usuarioId) {
        this.saldoAtual = saldoAtual;
        this.salarioMensal = salarioMensal;
        this.usuarioId = usuarioId;
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
}
