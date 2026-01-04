package com.hub.hds.dto.formacao;

import com.hub.hds.models.formacao.Status;
import jakarta.validation.constraints.NotNull;

public record FormacaoRequest(
        String nomeCurso,
        String instituicao,

        @NotNull
        Status status,
        String periodoInicio,
        String periodoFim
) {
}
