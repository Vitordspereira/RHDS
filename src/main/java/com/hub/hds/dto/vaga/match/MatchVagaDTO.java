package com.hub.hds.dto.vaga.match;

public record MatchVagaDTO(
        Long idVaga,
        Long idCandidato,
        double  percentual,
        String nivel
) {
}
