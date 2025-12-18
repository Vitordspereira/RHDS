package com.hub.hds.dto.experiencia;

public record ExperienciaResponse(
        Long id_experiencia,
        String nome_empresa,
        String funcao,
        String descricao,
        String outras_experiencias,
        String habilidades,
        String periodo_inicio,
        String periodo_fim
) {}