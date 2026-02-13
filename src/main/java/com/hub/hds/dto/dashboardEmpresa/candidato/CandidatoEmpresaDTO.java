package com.hub.hds.dto.dashboardEmpresa.candidato;

import com.hub.hds.models.candidato.Genero;

public record CandidatoEmpresaDTO(
        Long idCandidato,
        String nomeCompleto,
        Genero genero,
        String cidade,
        String estado,
        boolean favoritado
) {
}
