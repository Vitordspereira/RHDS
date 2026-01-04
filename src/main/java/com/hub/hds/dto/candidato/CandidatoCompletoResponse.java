package com.hub.hds.dto.candidato;

import com.hub.hds.dto.experiencia.ExperienciaResponse;
import com.hub.hds.dto.formacao.FormacaoResponse;
import com.hub.hds.models.candidato.Genero;

import java.time.LocalDate;
import java.util.List;

public record CandidatoCompletoResponse(
        Long idCandidato,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        Genero genero,
        LocalDate dataNascimento,
        String cidade,
        String estado,
        List<ExperienciaResponse> experiencias,
        List<FormacaoResponse> formacoes) {
}
