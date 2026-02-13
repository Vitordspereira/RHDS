package com.hub.hds.dto.candidatura;

import com.hub.hds.models.candidatura.StatusCandidatura;
import jakarta.validation.constraints.NotNull;

public record CandidaturaStatus(
        @NotNull
        StatusCandidatura novoStatus,

        String observacoes
) {
}
