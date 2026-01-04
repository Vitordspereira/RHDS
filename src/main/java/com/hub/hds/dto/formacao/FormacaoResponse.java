package com.hub.hds.dto.formacao;

import com.hub.hds.models.formacao.Status;
import jakarta.validation.constraints.NotNull;

public record FormacaoResponse(
        Long idFormacao,
        String nomeCurso,
        String instituicao,

        @NotNull
        Status status,
        String periodoInicio,
        String periodoFim
) {
}
