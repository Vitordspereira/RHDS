package com.hub.hds.dto.formacao;

import com.hub.hds.models.formacao.Status;
import jakarta.validation.constraints.NotNull;

public record FormacaoResponse(
        Long id_formacao,
        String nome_curso,
        String instituicao,

        @NotNull
        Status status,
        String periodo_inicio,
        String periodo_fim
) {
}
