package com.hub.hds.dto.candidato;

import com.hub.hds.models.candidato.Genero;

public record CandidatoResponse(
        Long id_candidato,
        String nome_completo,
        String email,
        String telefone,
        String cpf,
        Genero genero,
        java.time.LocalDate data_nascimento,
        String cidade,
        String estado
) {
}
