package com.hub.hds.dto.candidatura;

import com.hub.hds.models.candidatura.StatusCandidatura;

public record CandidaturaResponse(
        Long idCandidatura,
        StatusCandidatura statusCandidatura
) {
}
