package com.lifeboard.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SaldoRequest(
        @NotNull(message = "O valor atual é obrigatório.")
        BigDecimal valor
) { }
