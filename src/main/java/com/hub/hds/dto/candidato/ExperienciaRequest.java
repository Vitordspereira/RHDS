package com.hub.hds.dto.candidato;

import java.time.LocalDate;

public record ExperienciaRequest(
        String empresa,
        String cargo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        Boolean atual
) {}

