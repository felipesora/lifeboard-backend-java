package com.lifeboard.mapper;

import com.lifeboard.dto.MetaFinanceiraRequestDTO;
import com.lifeboard.dto.MetaFinanceiraResponseDTO;
import com.lifeboard.model.Financeiro;
import com.lifeboard.model.MetaFinanceira;

public class MetaFinanceiraMapper {

    public static MetaFinanceiraResponseDTO toDTO(MetaFinanceira meta) {
        return new MetaFinanceiraResponseDTO(
                meta.getId(),
                meta.getValorMeta(),
                meta.getDataLimite(),
                meta.getStatus(),
                meta.getFinanceiro().getId()
        );
    }

    public static MetaFinanceira toEntity(MetaFinanceiraRequestDTO dto, Financeiro financeiro) {
        MetaFinanceira meta = new MetaFinanceira();
        meta.setNome(dto.getNome());
        meta.setValorMeta(dto.getValorMeta());
        meta.setDataLimite(dto.getDataLimite());
        meta.setFinanceiro(financeiro);
        return meta;
    }
}
