package com.hub.hds.dto.dashboardCandidato.ListarVagas;

import com.hub.hds.models.candidatura.Candidatura;
import com.hub.hds.models.candidatura.StatusCandidatura;

public record VagaCandidaturaDTO(
        Long idVaga,
        String cargo,
        String nomeEmpresa,
        StatusCandidatura statusCandidatura
) {
    public static VagaCandidaturaDTO fromEntity(Candidatura candidatura) {
        return new VagaCandidaturaDTO(
                candidatura.getVaga().getIdVaga(),
                candidatura.getVaga().getCargo(),
                candidatura.getVaga().getEmpresaNome(),
                candidatura.getStatusCandidatura()
        );
    }
}
