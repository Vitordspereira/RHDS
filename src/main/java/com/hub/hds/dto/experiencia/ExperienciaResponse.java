package com.hub.hds.dto.experiencia;

public record ExperienciaResponse(
        Long idExperiencia,
        String nomeEmpresa,
        String funcao,
        String descricao,
        String outrasExperiencias,
        String habilidades,
        String periodoInicio,
        String periodoFim
) {}