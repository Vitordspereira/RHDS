package com.hub.hds.dto.dashboardEmpresa.candidato;

import com.hub.hds.dto.dashboardCandidato.ExperienciaDTO;
import com.hub.hds.dto.dashboardCandidato.FormacaoDTO;
import com.hub.hds.models.candidato.Genero;

import java.util.List;

public record CandidatoPerfilDTO(
        Long idCandidato,
        String nomeCompleto,
        String telefone,
        Genero genero,
        String dataNascimento,
        String cidade,
        String estado,
        String resumoProfissional,
        List<ExperienciaDTO> experiencias,
        List<FormacaoDTO> formacoes,
        String videoApresentacao
) {
}
