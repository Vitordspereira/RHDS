package com.hub.hds.dto.dashboardCandidato;

import com.hub.hds.models.candidato.Experiencia;
import java.time.LocalDate;

public record ExperienciaDTO(
        Long id,
        String cargo,
        String empresa,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim
) {
    public static ExperienciaDTO fromEntity(Experiencia e) {
        return new ExperienciaDTO(
                e.getIdExperiencia(),
                e.getCargo(),
                e.getEmpresa(),
                e.getDescricao(),
                e.getDataInicio(),
                e.getDataFim()
        );
    }
}
