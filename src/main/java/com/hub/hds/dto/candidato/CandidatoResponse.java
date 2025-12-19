package com.hub.hds.dto.candidato;

import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.models.candidato.Genero;

import java.util.List;

public record CandidatoResponse(
        Long idCandidato,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        Genero genero,
        java.time.LocalDate dataNascimento,
        String cidade,
        String estado,
        List<ExperienciaResponse> experiencias
) {
}
