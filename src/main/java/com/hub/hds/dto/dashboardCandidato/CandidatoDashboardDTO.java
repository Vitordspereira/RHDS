package com.hub.hds.dto.dashboardCandidato;

import com.hub.hds.models.candidato.Genero;

import java.util.List;

public record  CandidatoDashboardDTO(
        Long idCandidato,
        String nomeCompleto,
        String cpf,
        String telefone,
        Genero genero,
        String dataNascimento,
        String cidade,
        String estado,
        String resumoProfissional,
        List<ExperienciaDTO> experiencias,
        List<FormacaoDTO> formacoes
) {}

