package com.hub.hds.dto.dashboardCandidato;

import com.hub.hds.models.candidato.Formacao;
import java.time.LocalDate;

public record FormacaoDTO(
        Long id,
        String curso,
        String instituicao,
        LocalDate dataInicio,
        LocalDate dataFim
) {
    public static FormacaoDTO fromEntity(Formacao f) {
        return new FormacaoDTO(
                f.getIdFormacao(),
                f.getCurso(),
                f.getInstituicao(),
                f.getDataInicio(),
                f.getDataFim()
        );
    }
}
