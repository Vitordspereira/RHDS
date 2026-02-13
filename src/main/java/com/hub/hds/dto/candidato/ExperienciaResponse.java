package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Experiencia;

import java.time.LocalDate;

public record ExperienciaResponse(

        Long idExperiencia,
        String empresa,
        String cargo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim

) {
    public ExperienciaResponse(Experiencia experiencia) {
        this(
                experiencia.getIdExperiencia(),
                experiencia.getEmpresa(),
                experiencia.getCargo(),
                experiencia.getDescricao(),
                experiencia.getDataInicio(),
                experiencia.getDataFim()
        );
    }
}

