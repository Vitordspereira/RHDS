package com.hub.hds.dto.vaga.match;

public record MatchVagaRecomendadoDTO(
        Long idVaga,
        String tituloVaga,
        double percentual,
        String nivel
) {
}
