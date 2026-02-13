package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.NivelFormacao;
import com.hub.hds.models.candidato.StatusFormacao;
import java.time.LocalDate;

public record FormacaoRequest(
        String instituicao,
        String curso,
        NivelFormacao nivelFormacao,
        StatusFormacao statusFormacao,
        LocalDate dataInicio,
        LocalDate dataFim
) {}
