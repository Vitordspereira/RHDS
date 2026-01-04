package com.hub.hds.dto.experiencia;

import jakarta.validation.constraints.NotBlank;

public record ExperienciaRequest(
        @NotBlank(message = "O nome da empresa é obrigatório.")
        String nomeEmpresa,

        @NotBlank(message = "A função exercida é obrigatória.")
        String funcao,

        String descricao,
        String outrasExperiencias,
        String habilidades,
        String periodoInicio,
        String periodoFim
) {}
