package com.hub.hds.dto.candidatura;

import jakarta.validation.constraints.NotNull;

public record CandidaturaRequest(

        @NotNull(message = "Candidato é obrigatório")
        Long candidatoId,

        @NotNull(message = "Vaga é obrigatória")
        Long vagaId
) {}
