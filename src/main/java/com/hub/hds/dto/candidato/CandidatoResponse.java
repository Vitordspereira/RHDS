package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;



public record CandidatoResponse(
        Long idCandidato,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        Genero genero,
        java.time.LocalDate dataNascimento,
        String cidade,
        String estado
) {
}
